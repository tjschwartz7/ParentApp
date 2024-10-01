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
                    Log.d(TAG, "Waiting... ");
                    byte[] buffer = new byte[256];
                    //This is our packet
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                    serverSocket.receive(request); //Blocking wait
                    //Check if we're disconnected to avoid needless writing
                    if(!Globals.getUDPIsConnected())
                        //We've just received a message, so we should be connected
                        Globals.setUDPIsConnected(true);
                    Log.d(TAG, "Bits received: " + buffer.toString());
                    //Reset our start time
                    Globals.setTimeOfLastUDPMessageSend(System.currentTimeMillis());
                }

            } catch (Exception e) {
                Log.e(TAG, "Server error: " + e.getMessage());
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