package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class UDPServerService extends Service {
    private static final String TAG = "UDPServerService";
    private static final int TCP_PORT = 13002;
    private static final int UDP_PORT = 13003;
    private volatile boolean isRunning;
    private final int socketTimeoutMillis = 20000; //20 seconds
    private Socket heartbeatSocket;
    private DatagramSocket videoSocket;
    private final String serverHostname = "headlesswifi";
    private byte[] receiveData = new byte[1500];
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Server starting");
        isRunning = true;

        new Thread(new ServerRunnable()).start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        try {
            if (videoSocket != null && !videoSocket.isClosed()) {
                videoSocket.close();
            }
            if (heartbeatSocket != null && !heartbeatSocket.isClosed()) {
                heartbeatSocket.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing socket: " + e.getMessage());
        }
        Log.d(TAG, "Server socket stopped");
        Globals.setClientIsConnected(false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }

    private class ServerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Globals.setUDPIsConnected(false); //We haven't connected yet
                UDPStateMachine();

            } catch (Exception e) {
                Log.e(TAG, "Server error: " + e.getMessage());
            }
        }
    }

    //State machine:
    // Loop:
    //  Loop:
    //      Connect state
    //  Initialize UDP stuff
    //  Loop:
    //      Send heartbeat
    //      Input state


    public void UDPStateMachine() {
        //isRunning assumed to be true, if it isn't this should all shut down anyway
        while(isRunning) {
            Log.d(TAG, "Waiting for connection to Nanny at "+serverHostname+":"+ TCP_PORT);
            while(isRunning) {

                try {
                    Thread.sleep(2000);
                    Connect();
                    break;
                }
                catch(Exception ex){
                    Log.e(TAG, "Exception: " + ex.getMessage());
                }
            }
            try {
                Globals.setUDPIsConnected(true);
                //There's some stuff we only want to do once - do it in here
                InitializeDatagram();
                while(isRunning) {
                    SendHeartbeat();
                    InputState();
                }
            } catch(Exception ex) {
                Log.e(TAG, "Connection lost: " + ex.getMessage());

            } finally {
                //Make sure we close our sockets!
                try {
                    if(heartbeatSocket != null && !heartbeatSocket.isClosed()) {
                        heartbeatSocket.close();
                    }
                    if(videoSocket != null && !videoSocket.isClosed()) {
                        videoSocket.close();
                    }
                }catch(Exception e) {
                    Log.e(TAG, "Exception closing socket: " + e.getMessage());
                }
                Globals.setUDPIsConnected(false);
            }
        }



    }

    private void SendHeartbeat() throws IOException {
        // Send the IP address to the server
        DataOutputStream heartbeatStream = new DataOutputStream(heartbeatSocket.getOutputStream());
        //Send heartbeat
        heartbeatStream.writeUTF("1");
        Log.d(TAG, "Data sent to client.");
        Log.d(TAG, "Connected to server at " + serverHostname + ":" + TCP_PORT);
    }


    private void InputState() throws IOException {
        // Prepare to receive data
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        // Receive packet from client
        videoSocket.receive(receivePacket);
        // Convert received data to string
        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
        Log.d(TAG, "Received from client: " + receivedMessage);
    }

    private void InitializeDatagram() throws IOException {
        videoSocket = null;
        //Listen on SERVER_PORT under all IP addresses on the network
        InetAddress bindAddress = InetAddress.getByName("0.0.0.0");
        // Create a DatagramSocket to listen on port 13002
        videoSocket = new DatagramSocket(UDP_PORT, bindAddress);
        videoSocket.setSoTimeout(socketTimeoutMillis); //If it times out, let the function return
        Log.d(TAG, "Listening on hostname: " + bindAddress.getHostName());
    }

    private void Connect() throws IOException, UnknownHostException
    {
        heartbeatSocket = null;
        // Connect to the server
        heartbeatSocket = new Socket(serverHostname, TCP_PORT);
        heartbeatSocket.setSoTimeout(socketTimeoutMillis);  // Set a 20-second timeout
        Log.d(TAG, "Connected to server at " + serverHostname + ":" + TCP_PORT);

        // Send the IP address to the server
        DataOutputStream outputStream = new DataOutputStream(heartbeatSocket.getOutputStream());
        outputStream.writeUTF("syn");
        Log.d(TAG, "Data sent to client.");

        // Reading response from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(heartbeatSocket.getInputStream()));
        String response = in.readLine();  // Read server response (wait up to 5 seconds)
        Log.d(TAG, "Response from server: " + response);

        // Close the streams
        outputStream.close();
        in.close();
    }
}
