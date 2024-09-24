package com.example.baselineapp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerService extends Service {
    private static final String TAG = "TcpServerService";
    private static final int SERVER_PORT = 13000; // Your chosen port number
    private ServerSocket serverSocket;
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
            Log.e(TAG, "Error closing server socket: " + e.getMessage());
        }
        Log.d(TAG, "Server stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }

    private class ServerRunnable implements Runnable {
        @Override
        public void run() {
            try {

                serverSocket = new ServerSocket(SERVER_PORT, 5, InetAddress.getByName("0.0.0.0"));
                Log.d(TAG, "Server is listening on port " + SERVER_PORT + " On IP " + serverSocket.getInetAddress());

                while (isRunning) {
                    Log.d(TAG, "Waiting... ");
                    Socket clientSocket = serverSocket.accept(); // Accept incoming connections
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
                //Shouldn't need these
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        ClientHandler(in);
                    }
                });

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        ServerHandler(out);
                    }
                });
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


        private static void ClientHandler(BufferedReader in) {
            try {
                String message;
                while(Globals.userLoggedIn()) {
                    message = in.readLine();

                    Log.d(TAG, "Received: " + message);
                    String str_data;
                    //First 4 characters is the code
                    switch(message.charAt(0)) {
                        case '0':
                            Log.d(TAG, "Received TEMPERATURE data ");
                            str_data = message.substring(2); //Get string after first code and space
                            try {
                                double dbl_data = Double.valueOf(str_data);
                                Globals.setTempVal((int)(dbl_data*100) / 100.0);
                            }
                            catch(Exception ex) {
                                Log.e(TAG, "Message error: " + ex.getMessage());
                            }

                            break;
                        case '1':
                            Log.d(TAG, "Received PULSE data ");
                            str_data = message.substring(2); //Get string after first code and space
                            try {
                                double dbl_data = Double.valueOf(str_data);
                                Globals.setPulseVal((int)(dbl_data*100) / 100.0);
                            }
                            catch(Exception ex) {
                                Log.e(TAG, "Message error: " + ex.getMessage());
                            }
                            break;
                        case '2':
                            Log.d(TAG, "Received BLOOD OX data ");
                            str_data = message.substring(2); //Get string after first code and space
                            try {
                                double dbl_data = Double.valueOf(str_data);
                                Globals.setBloodOxVal((int)(dbl_data*100) / 100.0);
                            }
                            catch(Exception ex) {
                                Log.e(TAG, "Message error: " + ex.getMessage());
                            }
                            break;
                    }
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
