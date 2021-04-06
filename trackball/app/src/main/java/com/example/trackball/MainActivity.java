package com.example.trackball;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RelativeLayout virtualTrackball = null;                  // Grab outer circle layout
    private static InnerTrackball ball;                     // Grab inner circle layout
    Socket_Service mBoundService;
    ArrayList<Integer> list = new ArrayList<>();
    protected static boolean mRunning = false;

    /** Client Socket connection establishment using Service */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBoundService = ((Socket_Service.LocalBinder) service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBoundService = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(MainActivity.this, Socket_Service.class), mConnection, Context.BIND_AUTO_CREATE);
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
        virtualTrackball = findViewById(R.id.virtualTrackball);

        ball = findViewById(R.id.ball);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final String hostname = getIntent().getStringExtra("IP");       // IP address of the server received to establish connection
        startService(new Intent(MainActivity.this, Socket_Service.class).putExtra("IP",hostname));
    //    mBoundService.sendMessage("20,40");
         doBindService();                                             // initiate client socket

        Button leftClick = (Button) findViewById(R.id.L_btn);
        leftClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {                         // Left click message sent to the server to execute a left click
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

        Button rightClick = (Button) findViewById(R.id.R_btn);
        rightClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {                        // Right click message sent to the server to execute a right click
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

        virtualTrackball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {                       // Detect Finger touch movements on the inner (smaller) trackball
                if (ball==null)
                    ball = (InnerTrackball) virtualTrackball.getChildAt(1);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ball.onTouchEvent(event);
                        list = checkEvent(ball,event);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        list = checkEvent(ball,event);
                        if (mBoundService != null) {

                                 //System.out.println("not null");
                                 mBoundService.sendMessage((list.get(0) + "," + list.get(1)).toString());  // Send x and y coordinates over the socket to server using service

            }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        ball.setX(virtualTrackball.getPivotX()-ball.getWidth()/2);
                        ball.setY(virtualTrackball.getPivotY()-ball.getHeight()/2);

                        break;
                }
                return true;

            }
        });


    }
    private ArrayList<Integer> checkEvent(View ball, MotionEvent event){
        int xBall = (int) event.getX() - ball.getWidth() / 2;                    // Set the position of the inner trackball and get the x and y coordinates of it.
        int yBall = (int) event.getY() - ball.getHeight() / 2;
        if (xBall < 0)
            xBall = 0;
        if (yBall < 0)
            yBall = 0;
        if (event.getX() >= (virtualTrackball.getWidth() - ball.getWidth() / 2))
            xBall = virtualTrackball.getWidth() - ball.getWidth();
        if (event.getY() >= (virtualTrackball.getHeight() - ball.getWidth() / 2))
            yBall = virtualTrackball.getHeight() - ball.getHeight();

        ball.setX(xBall);
        ball.setY(yBall);
        int pos = getPos((int) event.getX(), (int) event.getY());
        System.out.println(pos + "POS");

       ArrayList<Integer> l= new ArrayList<>();
       l.add(xBall);
       l.add(yBall);
       return l;
    }
    public int getPos(int x, int y){
        int xPos = -1,yPos = -1;                  // Gets the position of the inner ball
        if (x<0) xPos = 1;
        if (y<0) yPos = 1;
        if (x>=virtualTrackball.getWidth()-ball.getWidth()/2) xPos = 3;
        if (y>=virtualTrackball.getWidth()-ball.getHeight()/2) yPos = 3;
        if (xPos==-1){
            xPos = x/(virtualTrackball.getWidth()/3)+1;
        }
        if (yPos==-1){
            yPos = y/(virtualTrackball.getHeight()/3)+1;
        }
        //Key 3*3
        return 3*(yPos-1)+xPos;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop_btn:
                if (mBoundService != null) {
                    mBoundService.sendMessage(("stop").toString());
                }
                break;

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}


