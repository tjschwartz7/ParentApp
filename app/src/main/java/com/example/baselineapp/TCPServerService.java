package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.*;
import java.nio.charset.Charset;

public class TCPServerService extends Service {
    private static final String TAG = "TcpServerService";
    private static final int SERVER_PORT = 13000;
    private ServerSocket serverSocket;
    private boolean isRunning;
    private static int previousStatusPacket = 0;


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
            Log.e(TAG, "Error closing server socket: " + e.getMessage());
        }
        Log.d(TAG, "Server stopped");
        Globals.setClientIsConnected(false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }

    private class ServerRunnable implements Runnable {
        @Override
        public void run() {
            Globals.setClientIsConnected(false);
            try {

                serverSocket = new ServerSocket(SERVER_PORT, 5, InetAddress.getByName("0.0.0.0"));
                Log.d(TAG, "Server is listening on port " + SERVER_PORT + " On IP " + serverSocket.getInetAddress());

                while (isRunning) {
                    Log.d(TAG, "Waiting... ");
                    Socket clientSocket = serverSocket.accept(); // Accept incoming connections
                    Globals.setClientIsConnected(true);
                    Log.d(TAG, "Client connected: " + clientSocket.getInetAddress());

                    new ClientHandler(clientSocket).run();
                }

            } catch (Exception e) {
                Log.e(TAG, "Server error: " + e.getMessage());
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                ServerHandler(out);
                handleClient(in);

            } catch (Exception e) {
                Log.e(TAG, "Client error: " + e.getMessage());
            } finally {
                try {
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error closing client socket: " + e.getMessage());
                }
            }
        }

        private static void handleClient(BufferedReader in) {
            try {
                String message;
                Log.d(TAG, "User logged in: "+Globals.userLoggedIn());
                while(Globals.userLoggedIn()) {
                    message = in.readLine();

                    Log.d(TAG, ""+message);

                    byte[] byte_message = message.getBytes(Charset.defaultCharset());
                    int command = byte_message[0];
                    Log.d(TAG, "Command: "+command);

                    boolean temperatureSensorWorking = (command & 0x1) == 1;
                    boolean bloodOxSensorWorking = (command & 0x2) == 2;
                    int temp = byte_message[1];
                    int pulse = byte_message[2];
                    int bloodOx = byte_message[3];



                    //It's important to execute this before we set the previousStatusPacket
                    //This will ensure that any notifications sent will have updated information
                    Globals.setTempVal(temp);
                    Globals.setBloodOxVal(bloodOx);
                    Globals.setPulseVal(pulse);
                    Globals.setTempSensorStatus(temperatureSensorWorking);
                    Globals.setPulseOxSensorStatus(bloodOxSensorWorking);

                    if(previousStatusPacket != command)
                    {
                        Globals.setPacifierWarningNotified(false);
                    }

                    previousStatusPacket = command;
                }
            } catch (Exception e) {
                Log.e(TAG, "Client error: " + e.getMessage());
            }
        }



        private static void ServerHandler(PrintWriter out)
        {
            try {
                if(Globals.getShutdownCommandStatus())
                {
                    out.println("0\n"); //Shutdown code
                    Globals.sendShutdownCommand(false);
                }
                else if(Globals.getPowerEnableStatus())
                {
                    out.println("1\n"); //Power On code
                    Globals.sendPowerEnableCommand(false);
                }


            }
            catch(Exception ex) {
                Log.e(TAG, "Server error: " + ex.getMessage());
            }
        }


    }
}
