package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPServerService extends Service {
    private static final String TAG = "UDPServerService";
    private static final int SERVER_PORT = 13002; // Our port we're using
    private DatagramSocket serverSocket;
    private volatile boolean isRunning;

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
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
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

                //This is our starting point.
                //Give the program five seconds to try and connect
                //before we send off any warnings.
                Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());
                UDPStateMachine();

            } catch (Exception e) {
                Log.e(TAG, "Server error: " + e.getMessage());
            }
        }
    }

    private boolean WaitingState() {
        String serverHostname = "headlesswifi.local";  // Remote host
        int serverPort = 13000;  // Replace with the remote server port
        Socket socket = null;

        try {
            // Connect to the server
            socket = new Socket(serverHostname, serverPort);
            socket.setSoTimeout(5000);  // Set a 5-second timeout
            Log.d(TAG, "Connected to server at " + serverHostname + ":" + serverPort);

            // Send the IP address to the server
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("syn");
            Log.d(TAG, "Data sent to client.");

            // Reading response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();  // Read server response (wait up to 5 seconds)
            Log.d(TAG, "Response from server: " + response);

            // Close the streams
            outputStream.close();
            in.close();

            return true;  // Successfully communicated

        } catch (UnknownHostException e) {
            //Log.e(TAG, "Unable to find local host.");
        } catch (IOException e) {
            //Log.e(TAG, "IO error occurred: " + e.getMessage());
        } finally {
            // Ensure socket is closed
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                    Log.d(TAG, "Connection closed.");
                } catch (IOException ex) {
                    Log.e(TAG, "IO Exception closing socket: " + ex.getMessage());
                }
            }
        }

        return false;  // Failed to communicate
    }

    public void UDPStateMachine()
    {
        while(isRunning) {
            while(!WaitingState()) {
                try {
                    Thread.sleep(200);
                }
                catch(Exception ex){
                    Log.e(TAG, "Exception: " + ex.getMessage());
                }
            }
            InputState();
        }



    }

    private void InputState()
    {
        serverSocket = null;
        try {
            //Listen on SERVER_PORT under all IP addresses on the network
            InetAddress bindAddress = InetAddress.getByName("0.0.0.0");
            Log.d(TAG, bindAddress.getHostName());


            // Create a DatagramSocket to listen on port 13002
            serverSocket = new DatagramSocket(SERVER_PORT, bindAddress);
            serverSocket.setSoTimeout(5000); //If it times out, let the function return

            byte[] receiveData = new byte[1024];
            while (isRunning) {

                // Prepare to receive data
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Receive packet from client
                serverSocket.receive(receivePacket);
                // Get client's address and port
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Convert received data to string
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Log.d(TAG, "Received from client: " + receivedMessage);

                //Check if we're disconnected to avoid needless writing
                if(!Globals.getUDPIsConnected())
                    //We've just received a message, so we should be connected
                    Globals.setUDPIsConnected(true);
                //Reset our start time
                Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());
            }

        } catch (SocketTimeoutException ste) {
            Log.e(TAG, "UDP Timed out: " + ste.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}