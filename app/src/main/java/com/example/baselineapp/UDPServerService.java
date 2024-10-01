package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServerService extends Service {
    private static final String TAG = "UDPServerService";
    private static final int SERVER_PORT = 13000; // Our port we're using
    private DatagramSocket serverSocket;
    private boolean isRunning;

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
                //Our server socket connected to our local INET device on SERVER_PORT
                serverSocket = new DatagramSocket(SERVER_PORT, InetAddress.getByName("0.0.0.0"));
                Log.d(TAG, "Server is listening on port " + SERVER_PORT + " On IP " + serverSocket.getInetAddress());

                //This is our starting point.
                //Give the program five seconds to try and connect
                //before we send off any warnings.
                Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());
                //This thread will keep track of whether or not we're connected to the client
                //If no message is received for 5 seconds, we're timed out

                heartbeat();

                while (isRunning) {

                    HandleRequest();

                    //Check if we're disconnected to avoid needless writing
                    if(!Globals.getUDPIsConnected())
                        //We've just received a message, so we should be connected
                        Globals.setUDPIsConnected(true);
                    //Reset our start time
                    Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());
                }

            } catch (Exception e) {
                Log.e(TAG, "Server error: " + e.getMessage());
            }
        }
    }


    public void HandleRequest()
    {
        final int SERVER_PORT = 13007;
        DatagramSocket socket = null;
        try {
            InetAddress bindAddress = InetAddress.getByName("0.0.0.0");
            System.out.println(bindAddress.getHostName());

            // Create a DatagramSocket to listen on port 13002
            socket = new DatagramSocket(SERVER_PORT, bindAddress);
            byte[] receiveData = new byte[1024];

            System.out.println("UDP Server is listening on port " + SERVER_PORT);
            System.out.println("Inet connection: " + socket.getInetAddress());

            while (isRunning) {
                // Prepare to receive data
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Receive packet from client
                socket.receive(receivePacket);
                // Get client's address and port
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Convert received data to string
                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Log.d(TAG, "Received from client: " + receivedMessage);

                // Send response back to the client
                String responseMessage = "Hello from the server!";
                byte[] sendData = responseMessage.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    public void heartbeat()
    {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while(isRunning)
                {
                    //Set to current time - time of last message sent
                    Globals.setTimeSinceLastUDPMessageSent(System.currentTimeMillis() - Globals.getTimeOfLastUDPMessageSend());
                    //No message received in 5 seconds
                    if(Globals.getTimeSinceLastUDPMessageSent() > 5000)
                    {
                        //We're timed out
                        Globals.setUDPIsConnected(false);
                    }

                    //Can't sleep without a try block
                    try
                    {
                        // Sleep for 1 second to avoid busy waiting
                        Thread.sleep(1*1000);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}