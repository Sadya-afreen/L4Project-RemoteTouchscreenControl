package com.example.client_point_movetechnique;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketService extends Service {


        String hostname=null;
        private static final int SERVER_PORT = 5000;
        PrintWriter out;
        Socket socket;
        private static final String serverAddr = "192.168.1.5";

        private final IBinder mBinder = new LocalBinder();
        private MainActivity main_activity;
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            System.out.println("I am in Ibinder onBind method");
            return mBinder;
        }
        public class LocalBinder extends Binder {
            public SocketService getService() {
                System.out.println("I am in Localbinder ");
                return SocketService.this;

            }
        }

        @Override
        public void onCreate() {
            super.onCreate();
            System.out.println("I am in on create");
        }

        public void IsBoundable(){
            Toast.makeText(this,"I bind like butter", Toast.LENGTH_LONG).show();
        }

        public void sendMessage(String message){
            if (out != null && !out.checkError()) {
                System.out.println("in sendMessage"+message);
                out.println(message);
                out.flush();
            }
        }
        @Override
        public int onStartCommand(Intent intent,int flags, int startId){
            super.onStartCommand(intent, flags, startId);
            System.out.println("I am in on start");
            hostname = intent.getStringExtra("IP");
            System.out.println(hostname);
            //  Toast.makeText(this,"Service created ...", Toast.LENGTH_LONG).show();
            Runnable connect = new connectSocket();
            new Thread(connect).start();
            return Service.START_NOT_STICKY;
        }

        private class connectSocket implements Runnable {

            @Override
            public void run() {


                try {
                    //here you must put your computer's IP address.
                    //  serverAddr = "192.168.1.5";
                    Log.e("TCP Client", "C: Connecting...");
                    //create a socket to make the connection with the server

                    socket = new Socket(hostname, SERVER_PORT);

                    try {


                        //send the message to the server
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);


                        Log.e("TCP Client", "C: Sent.");

                        Log.e("TCP Client", "C: Done.");


                    }
                    catch (Exception e) {

                        Log.e("TCP", "S: Error", e);

                    }
                } catch (Exception e) {

                    Log.e("TCP", "C: Error", e);

                }

            }

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            try {
                socket.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            socket = null;
        }



    }




