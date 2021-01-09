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

    /* variable for counting two successive up-down events */
    int clickCount = 0;
    /*variable for storing the time of first click*/
    long startTime;
    /* variable for calculating the total time*/
    long duration;
    /* constant for defining the time duration between the click that can be considered as double-tap */
    static final int MAX_DURATION = 500;



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
            Log.i("mbound", "obund");
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
        final String hostname = getIntent().getStringExtra("IP");
//        Log.d("AirMouse Hostname",hostname);
        startService(new Intent(MainActivity.this,Socket_BackgroundService.class).putExtra("IP",hostname));
        doBindService();

        Button leftClick = (Button) findViewById(R.id.l_btn);
        leftClick.setOnTouchListener(new View.OnTouchListener() {
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
        rightClick.setOnTouchListener(new View.OnTouchListener() {
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
        final Button cursor_button = (Button) findViewById(R.id.cursor_btn);


        touch_pad = (TextView) findViewById(R.id.textView);


//my_mouse_pad
        touch_pad.setOnTouchListener(new View.OnTouchListener() {
            long down;
//            final Handler handler = new Handler();
//            Runnable mLongPressed = new Runnable() {
//                public void run() {
//                    Log.i("", "Long press!");
//                }
//            };
       //     ArrayList<String> list = new ArrayList<>();
       //     MessageSender messagesender = new MessageSender();
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isConnected = true;
                    int pointerCount = event.getPointerCount();
                    if (isConnected) {
                        //  if(pointerCount==1){
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                //   handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());
                                //save X and Y positions when user touches the TextView
//                                lastDownX = (int) event.getX();
//                                lastDownY = (int) event.getY();
                                isMoving = false;
//                                startTime = System.currentTimeMillis();
//                                clickCount++;
//                                lastTouchDownXY[0] = (int)event.getX();
//                                lastTouchDownXY[1] = (int)event.getY();
                                initX = (int) event.getX();
                                initY = (int) event.getY();

                      //          onSingClick(event, true);
                                //mDownTime = System.currentTimeMillis();
//                                mDownX = (int)event.getX();
//                                mDownY = (int)event.getY();
//                                System.out.println(mX+"x" + mY+"y");
//                                if(mLastMoveX-mX >10 &&mLastMoveY-mY >10 ){
//                                if (mBoundService != null) {
//
//                                    mBoundService.sendMessage(((int)(mX/3.2f) + "," + (int)(mY/3.2f)).toString());
//
//                                }
//                                else{
//                                if (mBoundService != null) {
//
//                                    mBoundService.sendMessage(((int)(mX/7.2f) + "," + (int)(mY/7.2f)).toString());
//
//                                }
//                                }
//                                initX = event.getX();
//                                initY = event.getY();
                                mouseMoved = false;
                                break;
                            case MotionEvent.ACTION_MOVE:
//                                paint.setColor(Color.RED);
//                                x = (int) event.getRawX();
//                                y = (int) event.getRawY();
//
//                                if (lastDownX < x)
//                                    disX += (x - lastDownX) / scrollDrag;
//                                else
//                                    disX -= (lastDownX - x) / scrollDrag;
//
//                                if (lastDownY < y)
//                                    disY += (y - lastDownY) / scrollDrag;
//                                else
//                                    disY -= (lastDownY - y) / scrollDrag;
//
//                                if (disX < 0) disX = 0;
//                                if (disY < 0) disY = 0;
//
//                                lastX = x;
//                                lastY = y;
//                            //    if(disX!=0 && disY!=0) {
//                                    if (mBoundService != null) {
//
//                                        mBoundService.sendMessage((disX + "," + disY).toString());
//
//                                    }
//                             //   }
//                                mouseMoved = true;
//                                break;
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

                                        mBoundService.sendMessage((disX + "," + disY).toString());

                                    }
                                }
//                                if (mLastMoveX != Float.MAX_VALUE && mLastMoveY != Float.MAX_VALUE) {
//
//                                    //trial
//                                    disX = (int) ((int)event.getX() - mDownX); //Mouse movement in x direction
//                                    disY = (int) ((int)event.getY() - mDownY); //Mouse movement in y direction
//                                    mDownX = event.getX();
//                                    mDownY = event.getY();
//////                                    disX = (int) ((int)initX - mDownX); //Mouse movement in x direction
//////                                    disY = (int) ((int) initY - mDownY); //Mouse movement in y direction
//                                }
//                                int distance = (int) Math.sqrt(disX * disX + disY * disY);
//                                if (distance > 100 || System.currentTimeMillis() - mLastMoveTime > 100) {
//                                    if(disX!=0 || disY!=0) {
//                                        if (mBoundService != null) {
//
//                                            mBoundService.sendMessage((disX + "," + disY).toString());
//
//                                        }
//                                    }
//                                    //   sendCommand(OperationData.OPERATION_MOVE, distanceX, distanceY, null);
//                                    mLastMoveX = initX;
//                                    mLastMoveY = initY;
//
//////                                    lX=disX;
//////                                    lY=disY;
//                                    mLastMoveTime = System.currentTimeMillis();
//                                }
////                                System.out.println(touch_pad.getLayout().getWidth());
//                                //if(touch_pad.getLayout().getWidth())
//                               // c.drawCircle(initX, initY, 30, paint);
//
////                                cursor_button.setX(initX);
////                                cursor_button.setY(initY);

                                mouseMoved = true;
                                break;


//                                disX = (int)(event.getX() - initX); //Mouse movement in x direction
//                                disY = (int)(event.getY() - initY); //Mouse movement in y direction
//                            /*set init to new position so that continuous mouse movement
//                            is captured*/
//                                initX = event.getX();
//                                initY = event.getY();
//                                if (disX != 0 || disY != 0) {
//                                    //out.println(disX + "," + disY); //send mouse movement to server
//
////                                    cursor_button.setX(initX);
////                                    cursor_button.setY(initY);
//                                    if (mBoundService != null) {
//                                        mBoundService.sendMessage((disX + "," + disY).toString());
//                                    }
//                                }
//                                //    list.add((disX + "," + disY + ",").toString());
//
//                            //messagesender.execute((disX +"," + disY).toString());
//
//                                mouseMoved = true;
//                                break;
                            case MotionEvent.ACTION_UP:
                                lastUpX = (int) event.getX();
                                lastUpY = (int) event.getY();
                                clickCount++;
                                //  long time = System.currentTimeMillis() - startTime;
                                //  duration = duration + time;
                                if (clickCount == 1) {
                                    startTime = System.currentTimeMillis();
                                } else if (clickCount == 2) {
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

//           touch_pad.setOnLongClickListener(new View.OnLongClickListener() {
//               @Override
//               public boolean onLongClick(View v) {
//
//                   // retrieve the stored coordinates
//                   float x = lastTouchDownXY[0];
//                   float y = lastTouchDownXY[1];
//
//                   if (mBoundService != null) {
//
//                       mBoundService.sendMessage(((int)(x) + "," + (int)(y)).toString());
//
//                                }
//                   // we have consumed the touch event
//                   return true;
//               }
//           });
//    public void Send(View v){
//        MessageSender messagesender = new MessageSender();
//        messagesender.execute(tv1.getText().toString());
//    }

    }
    private boolean held_too_long( MotionEvent event){
        return event.getEventTime()-time_0 > 3;

    }


    private void down(MotionEvent touch)
    {
        time_0= touch.getEventTime();
        x_0= touch.getX();
        y_0= touch.getY();
    }




        private void onSingClick(MotionEvent event, boolean down) {

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

    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.stop_btn:
                if (mBoundService != null) {
            mBoundService.sendMessage(("stop").toString());
        }
            break;
//            case R.id.r_btn:
//                if (mBoundService != null) {
//                    mBoundService.sendMessage(("rightclick").toString());
//                }
//                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (isConnected && out != null) {
//            try {
//                out.println("exit"); //tell server to exit
//                s.close(); //close socket
//            } catch (IOException e) {
//                Log.e("remotedroid", "Error in closing socket", e);
//            }
//        }
//    }

}