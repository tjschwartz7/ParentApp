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
    private static final int SERVER_PORT = 13002; // Our port we're using
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

                //This is our starting point.
                //Give the program five seconds to try and connect
                //before we send off any warnings.
                Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());

                //This is a blocking function
                HandleRequest();




            } catch (Exception e) {
                Log.e(TAG, "Server error: " + e.getMessage());
            }
        }
    }


    public void HandleRequest()
    {
        DatagramSocket socket = null;
        try {
            //Listen on SERVER_PORT under all IP addresses on the network
            InetAddress bindAddress = InetAddress.getByName("0.0.0.0");
            Log.d(TAG, bindAddress.getHostName());


            // Create a DatagramSocket to listen on port 13002
            socket = new DatagramSocket(SERVER_PORT, bindAddress);
            byte[] receiveData = new byte[1024];

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

                //Check if we're disconnected to avoid needless writing
                if(!Globals.getUDPIsConnected())
                    //We've just received a message, so we should be connected
                    Globals.setUDPIsConnected(true);
                //Reset our start time
                Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());
            }

            socket.close();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}