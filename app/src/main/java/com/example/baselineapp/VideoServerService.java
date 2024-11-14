package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class VideoServerService extends Service {
    private static final String TAG = "UDPServerService";
    private static final int TCP_PORT = 13002;
    private static final int UDP_PORT = 13003;
    private volatile boolean isRunning;
    private final int socketTimeoutMillis = 20000; //20 seconds
    private Socket connectionSocket;

    //Use this hostname on wifi
    //private final String serverHostname = "nanny";

    //Use this hostname on hotspots
    private final String serverHostname = "nanny.local";
    private final byte[] receiveData = new byte[1500];
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
            if (connectionSocket != null && !connectionSocket.isClosed()) {
                connectionSocket.close();
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
            try {
                //If we just failed to connect, wait a few seconds before trying again
                Thread.sleep(2000);
                Connect();
                break;
            }
            catch(Exception ex){
                Log.e(TAG, "Exception: " + ex.getMessage());
            }

            //Attempt to close the socket
            try {
                if (connectionSocket != null && !connectionSocket.isClosed()) {
                    connectionSocket.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error closing socket: " + e.getMessage());
            }
        }



    }

    private void Connect() throws IOException, UnknownHostException
    {
        connectionSocket = null;
        // Connect to the server
        connectionSocket = new Socket(serverHostname, TCP_PORT);
        connectionSocket.setSoTimeout(socketTimeoutMillis);  // Set a 20-second timeout
        Log.d(TAG, "Connected to server at " + serverHostname + ":" + TCP_PORT);
    }

}
