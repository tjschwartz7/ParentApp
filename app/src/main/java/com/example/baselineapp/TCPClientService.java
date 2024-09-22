package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClientService extends Service {
    private static final String TAG = "TcpClientService";
    private static final String SERVER_IP = "192.168.0.30"; // Replace with your server's IP address
    private static final int SERVER_PORT = 13000; // Replace with your server's port number

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Thread clientThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Start the client thread to connect to the server
        clientThread = new Thread(new ClientRunnable());
        clientThread.start();

        // Keep service running until explicitly stopped
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the client thread and close resources
        disconnect();
        Log.d(TAG, "Service stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return null since this is a started service, not a bound service
        return null;
    }

    // Runnable that handles the TCP connection
    private class ClientRunnable implements Runnable {
        @Override
        public void run() {
            try {
                // Connect to the server
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(TAG, "Connected to the server");

                // Read messages from the server
                String message;
                while ((message = input.readLine()) != null) {
                    Log.d(TAG, "Received from server: " + message);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error connecting to server: " + e.getMessage());
            }
        }
    }

    // Disconnect from the server and close all resources
    private void disconnect() {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error closing connection: " + e.getMessage());
        }
    }


}
