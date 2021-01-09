package com.example.clientapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MessageSender extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);
        findViewById(R.id.bdone).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("IP",((EditText)findViewById(R.id.etIP)).getText().toString());
        startActivity(i);
    }

//    Socket s;
//    PrintWriter pw;
//    //private boolean isConnected = false;
//
//    @Override
//    protected Void doInBackground(ArrayList<String>... params) {
//
//    //    boolean result = true;
//        String message;
//
//        try {
//            s = new Socket("192.168.1.5", 5000);
//            pw = new PrintWriter(new BufferedWriter(((new OutputStreamWriter(s.getOutputStream())))));
//            for(int i = 0;i <params[0].size(); i++ ) {
//                message = params[0].get(i);
//                pw.write(message);
//                pw.flush();
//            }
//           pw.close();
//           s.close();
//        }
//        catch(IOException e){
//            e.printStackTrace();
//            Log.e("remotedroid", "Error while connecting", e);
//       //     result = false;
//        }
//        return null;
//    }
//
////    @Override
////    protected Boolean doInBackground(String... params) {
////        boolean result = true;
////        try {
////            s = new Socket(params[0], 5000);//Open socket on server IP and port
////        } catch (IOException e) {
////            Log.e("remotedroid", "Error while connecting", e);
////            result = false;
////        }
////        return result;
////    }
////
////    @Override
////    protected void onPostExecute(Boolean result) {
////        isConnected = result;
////
////        try {
////            if (isConnected) {
////                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true); //create output stream to send data to server
////            }
////        } catch (IOException e) {
////            Log.e("remotedroid", "Error while creating OutWriter", e);
////        }
////    }
////    @Override
////    protected void onPostExecute(Boolean result) {
////        isConnected = result;
////        try {
////            if (isConnected) {
////                pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s
////                        .getOutputStream())), true); //create output stream to send data to server
////            }
////        } catch (IOException e) {
////            Log.e("remotedroid", "Error while creating OutWriter", e);
////
////        }
////    }

}
