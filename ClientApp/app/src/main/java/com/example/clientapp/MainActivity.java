package com.example.clientapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static javax.xml.datatype.DatatypeConstants.DURATION;

public class MainActivity extends AppCompatActivity{

    //DataOutputStream dos
    Socket s;
    PrintWriter out;

    private boolean isConnected = false;
    private boolean mouseMoved = false;
    boolean isMoving = false;
    protected static boolean mRunning = false;
    private float initX = 0;
    private float initY = 0;


    private int x =0;
    private int y = 0;
    private int lastDownX = 0;
    private int lastDownY = 0;
    private int lastUpX = 0;
    private int lastUpY = 0;
    private int lastX = 0, lastY = 0;
    private int scrollDrag = 8;
    private int disX = 0;
    private int disY = 0;
//    private float disX = 0;
//    private float disY = 0;
    private float mLastMoveX = Float.MAX_VALUE;
    private float mLastMoveY = Float.MAX_VALUE;
    private long mLastMoveTime;
    private long mDownTime;
    private int mX;
    private int mY;
    private int lX;
    private int lY;
    private float mDownX;
    private float mDownY;

    private long mUpTime;
    private float mUpX;
    private float mUpY;
    // class member variable to save the X,Y coordinates
    private float[] lastTouchDownXY = new float[2];

    private long time_0;

    Paint paint = new Paint();
    Canvas c = new Canvas();
    //EditText e1;
    MotionEvent e;
    private RelativeLayout mylayout = null;

    TextView touch_pad;
    TextView tv1, tv2;
    private float x_0, y_0;

    /* variable for counting two successive tap events */
    int clickCount = 0;
    /*variable for storing the time of first click*/
    long startTime;
    /* variable for calculating the total time*/
    long duration;
    /* constant for defining the time duration between the click that can be considered as double-tap */
    static final int MAX_DURATION = 500;



     /** Client Socket connection establishment using Service */
    Socket_BackgroundService mBoundService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBoundService = ((Socket_BackgroundService.LocalBinder)service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBoundService = null;
        }
    };
    private void doBindService() {
        //final String hostname = getIntent().getStringExtra("IP");
        bindService(new Intent(MainActivity.this, Socket_BackgroundService.class), mConnection, Context.BIND_AUTO_CREATE);
        mRunning = true;
        if (mBoundService != null) {
            mBoundService.IsBoundable();
            Log.i("mbound", "bound");
            System.out.println("mbound");
        }
    }


    private void doUnbindService() {
        if (mRunning) {
            // Detach our existing connection.
            unbindService(mConnection);
            mRunning = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final String hostname = getIntent().getStringExtra("IP");        // IP address of the server received to establish connection
//        Log.d("AirMouse Hostname",hostname);
        startService(new Intent(MainActivity.this,Socket_BackgroundService.class).putExtra("IP",hostname));
        doBindService();

        Button leftClick = (Button) findViewById(R.id.l_btn);
        leftClick.setOnTouchListener(new View.OnTouchListener() {              // Left click message sent to the server to execute a left click
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("leftclick","");
                    if(mBoundService!=null){

                        mBoundService.sendMessage(("leftclick"));
                    }
                }

                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(mBoundService!=null){

                        mBoundService.sendMessage(("leftrelease"));
                    }
                }

                return true;
            }});

        Button rightClick = (Button) findViewById(R.id.r_btn);
        rightClick.setOnTouchListener(new View.OnTouchListener() {                        // Right click message sent to the server to execute a right click
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i("rightclick","");
                    if(mBoundService!=null){

                        mBoundService.sendMessage(("rightclick"));
                    }
                }

                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(mBoundService!=null){

                        mBoundService.sendMessage(("rightrelease"));
                    }
                }

                return true;
            }});
        mylayout = (RelativeLayout) findViewById(R.id.my_mouse_pad);

        touch_pad = (TextView) findViewById(R.id.textView);                 // Grab the text view layout for the trackpad


//my_mouse_pad
        touch_pad.setOnTouchListener(new View.OnTouchListener() {          // Detect Finger touch movements on the trackpad
            long down;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isConnected = true;
                    int pointerCount = event.getPointerCount();
                    if (isConnected) {
                        //  if(pointerCount==1){
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:              // Finger touch initiates tracking finger movement

                                isMoving = false;

                                initX = (int) event.getX();           // x and y coordinate of the point the finger touches the trackpad
                                initY = (int) event.getY();


                                mouseMoved = false;
                                break;
                            case MotionEvent.ACTION_MOVE:          // Finger touch initiates tracking relative finger movement to send x and y values over to the server.

                                isMoving = true;
//
                                int disX = 0;
                                int disY = 0;

                                disX = (int) (event.getX() - initX); //Mouse movement in x direction
                                disY = (int) (event.getY() - initY); //Mouse movement in y direction
                                initX = (int) event.getX();
                                initY = (int) event.getY();
                                if (disX != 0 || disY != 0) {
                                    if (mBoundService != null) {

                                        mBoundService.sendMessage((disX + "," + disY).toString());      // Send x and y coordinates over the socket to server using service

                                    }
                                }

////                                System.out.println(touch_pad.getLayout().getWidth());
//                                //if(touch_pad.getLayout().getWidth())
//                               // c.drawCircle(initX, initY, 30, paint);
//
////                                cursor_button.setX(initX);
////                                cursor_button.setY(initY);

                                mouseMoved = true;                    // variable to track finger movement
                                break;


                            case MotionEvent.ACTION_UP:                               // Finger moved from the trackpad detected to distinguish between relative and absolute cursor mode.
                                lastUpX = (int) event.getX();
                                lastUpY = (int) event.getY();
                                clickCount++;
                                //  long time = System.currentTimeMillis() - startTime;
                                //  duration = duration + time;
                                if (clickCount == 1) {                              // Checks if double tap occurred
                                    startTime = System.currentTimeMillis();
                                } else if (clickCount == 2) {                                // double tap checked for absolute cursor jump values to be sent to server.
                                    long duration = System.currentTimeMillis() - startTime;
                                    if (duration <= MAX_DURATION) {
                                        if (mBoundService != null) {

                                            mBoundService.sendMessage(((int) (lastUpX/30.2f) + "," + (int) (lastUpY/30.2f)).toString());

                                        }
                                        clickCount = 0;
                                        duration = 0;
                                    } else {
                                        clickCount = 1;
                                        startTime = System.currentTimeMillis();
                                    }


                                }


                                onSingClick(event, false);
//                               if (mBoundService != null) {
//
//                                   mBoundService.sendMessage(((int)(lastUpX/3.2f) + "," + (int)(lastUpY/3.2f)).toString());
//
//                               }
                                break;
                        }
                    }


                    // Toast.makeText(getApplicationContext(),"I was touched", Toast.LENGTH_SHORT).show();
                    //        }
                    return true;
                }

            });


    }
//    private boolean held_too_long( MotionEvent event){
//        return event.getEventTime()-time_0 > 3;
//
//    }
//
//    private void down(MotionEvent touch)
//    {
//        time_0= touch.getEventTime();
//        x_0= touch.getX();
//        y_0= touch.getY();
//    }

    private void onSingClick(MotionEvent event, boolean down) {        // Method to check previous cursor position to allow constant relative cursor mode updates to be sent.

    if (down) {
        mDownTime = System.currentTimeMillis();
        mDownX = event.getX();
        mDownY = event.getY();

    } else {
        mUpTime = System.currentTimeMillis();
        mUpX = event.getX();
        mUpY = event.getY();
    }


    }

    public void onClick(View v) {                     // Stop message sent to the server to disconnect it.
        switch(v.getId())
        {
            case R.id.stop_btn:
                if (mBoundService != null) {
            mBoundService.sendMessage(("stop").toString());
        }
            break;

        }
    }
    @Override                               // Socket service unbound to release the resource.
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


}