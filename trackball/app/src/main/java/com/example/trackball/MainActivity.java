package com.example.trackball;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RelativeLayout virtualTrackball = null;                  // Grab outer circle layout
    private static CircleTrackball ball;                     // Grab inner circle layout
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
         int backgroundTrackball = R.drawable.bg_trackball;
         int backgroundDpad = R.color.transparent;


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
            public boolean onTouch(View v, MotionEvent event) {                                          // Right click message sent to the server to execute a right click
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
                    ball = (CircleTrackball) virtualTrackball.getChildAt(1);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        ball.onTouchEvent(event);
                        list = checkEvent(ball,event);

                        break;
                    case MotionEvent.ACTION_MOVE:
                        list = checkEvent(ball,event);
                        if (mBoundService != null) {

                                 System.out.println("not null");
                                 mBoundService.sendMessage((list.get(0) + "," + list.get(1)).toString());  // Send x and y coordinates over the socket to server using service

            }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        ball.setX(virtualTrackball.getPivotX()-ball.getWidth()/2);
                        ball.setY(virtualTrackball.getPivotY()-ball.getHeight()/2);
//                        if (actionTrackball!=null){
//                            actionTrackball.onCancel();
//                        }
//                        if (pad!=null)
//                            pad.resetColor();
                        break;
                }
                return true;

            }
        });


    }
    private ArrayList<Integer> checkEvent(View ball, MotionEvent event){
        int xBall = (int) event.getX() - ball.getWidth() / 2;
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

//        if (mBoundService != null) {
       ArrayList<Integer> l= new ArrayList<>();
       l.add(xBall);
       l.add(yBall);
       return l;
//               System.out.println("not null");
//                mBoundService.sendMessage((xBall + "," + yBall).toString());
////
//            }
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
//    public static class VirtualTrackball extends RelativeLayout {
//
//        MainActivity t;
//    //    Socket_Service mBoundService;
//        private Context context;
//        //  private CircleTrackball ball;
//        //     private VirtualPad pad;
//        //  private IAction actionTrackball;
//        private boolean isDpad;
//
//        public VirtualTrackball(Context context) {
//            super(context);
//            initView(context);
//        }
//
//        public VirtualTrackball(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            initView(context);
//        }
//
//        public VirtualTrackball(Context context, AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            initView(context);
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//        public VirtualTrackball(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//            super(context, attrs, defStyleAttr, defStyleRes);
//            initView(context);
//        }
//
////        public IAction getActionTrackball() {
////            return actionTrackball;
////        }
////
////        public void setActionTrackball(IAction actionTrackball) {
////            this.actionTrackball = actionTrackball;
////        }
//
//        private void initView(Context context) {
//            this.context = context;
//            //    this.context.bindService(t.getIntent(),mConnection,Context.BIND_AUTO_CREATE);
//
//            //  pad = new VirtualPad(context);
//            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//            //  pad.setLayoutParams(params);
//            // pad.setVisibility(GONE);
//            // addView(pad);
//        }
//
////        public ArrayList<Integer> sendcoordinates() {
////        list.add(x);
////        list.add(y);
////        return list;
////        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            if (ball == null)
//                ball = (CircleTrackball) getChildAt(1);
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    ball.onTouchEvent(event);
//                    checkEvent(ball, event);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    checkEvent(ball, event);
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    ball.setX(getPivotX() - ball.getWidth() / 2);
//                    ball.setY(getPivotY() - ball.getHeight() / 2);
////                    if (actionTrackball != null) {
////                        actionTrackball.onCancel();
////                    }
////                    if (pad != null)
////                        pad.resetColor();
//                    break;
//            }
//            return true;
//        }
//
//        private void checkEvent(View ball, MotionEvent event) {
//            int xBall = (int) event.getX() - ball.getWidth() / 2;
//            int yBall = (int) event.getY() - ball.getHeight() / 2;
//            if (xBall < 0)
//                xBall = 0;
//            if (yBall < 0)
//                yBall = 0;
//            if (event.getX() >= (getWidth() - ball.getWidth() / 2))
//                xBall = getWidth() - ball.getWidth();
//            if (event.getY() >= (getHeight() - ball.getWidth() / 2))
//                yBall = getHeight() - ball.getHeight();
//
//            ball.setX(xBall);
//            ball.setY(yBall);
//            int pos = getPos((int) event.getX(), (int) event.getY());
//            System.out.println(pos + "pos");
//            System.out.println(t.mBoundService + "rfkjelrijf");
//            if (t.mBoundService != null) {
////              this.  ArrayList<Integer> l= virtualTrackball.sendcoordinates();
//                System.out.println("not null");
//                t.mBoundService.sendMessage((xBall + "," + yBall).toString());
//
//            }
//
////            list.add(xBall);
////            list.add(yBall);
//            //    doAction(pos);
//        }
//
////        private void doAction(int action) {
////            if (pad != null)
////                pad.resetColor();
////            try {
////                switch (action) {
////                    case ActionTrackball.ACTION_TOPLEFT:
////                        if (!isDpad)
////                            actionTrackball.onTopLeft();
////                        break;
////                    case ActionTrackball.ACTION_TOP:
////                        if (pad != null)
////                            pad.setBackgroundClicked(R.id.dpadUp);
////                        actionTrackball.onTop();
////                        break;
////                    case ActionTrackball.ACTION_TOPRIGHT:
////                        if (!isDpad)
////                            actionTrackball.onTopRight();
////                        break;
////                    case ActionTrackball.ACTION_LEFT:
////                        if (pad != null)
////                            pad.setBackgroundClicked(R.id.dpadLeft);
////                        actionTrackball.onLeft();
////                        break;
////                    case ActionTrackball.ACTION_CENTER:
////                        actionTrackball.onCenter();
////                        break;
////                    case ActionTrackball.ACTION_RIGHT:
////                        if (pad != null)
////                            pad.setBackgroundClicked(R.id.dpadRight);
////                        actionTrackball.onRight();
////                        break;
////                    case ActionTrackball.ACTION_BOTTOMLEFT:
////                        if (!isDpad)
////                            actionTrackball.onBottomLeft();
////                        break;
////                    case ActionTrackball.ACTION_BOTTOM:
////                        if (pad != null)
////                            pad.setBackgroundClicked(R.id.dpadDown);
////                        actionTrackball.onBottom();
////                        break;
////                    case ActionTrackball.ACTION_BOTTOMRIGHT:
////                        if (!isDpad)
////                            actionTrackball.onBottomRight();
////                        break;
////                }
////            } catch (Exception e) {
////            }
////        }
//
//        private int getPos(int x, int y) {
//            int xPos = -1, yPos = -1;
//            if (x < 0) xPos = 1;
//            if (y < 0) yPos = 1;
//            if (x >= getWidth() - ball.getWidth() / 2) xPos = 3;
//            if (y >= getWidth() - ball.getHeight() / 2) yPos = 3;
//            if (xPos == -1) {
//                xPos = x / (getWidth() / 3) + 1;
//            }
//            if (yPos == -1) {
//                yPos = y / (getHeight() / 3) + 1;
//            }
//            //Key 3*3
//            return 3 * (yPos - 1) + xPos;
//        }
////        public interface IAction {
////            void onTop();
////
////            void onBottom();
////
////            void onLeft();
////
////            void onRight();
////
////            void onTopLeft();
////
////            void onTopRight();
////
////            void onBottomLeft();
////
////            void onBottomRight();
////
////            void onCenter();
////
////            void onCancel();
////        }
//
////            public abstract class ActionTrackball {
////                public static final int ACTION_TOPLEFT = 1;
////                public static final int ACTION_TOP = 2;
////                public static final int ACTION_TOPRIGHT = 3;
////                public static final int ACTION_LEFT = 4;
////                public static final int ACTION_CENTER = 5;
////                public static final int ACTION_RIGHT = 6;
////                public static final int ACTION_BOTTOMLEFT = 7;
////                public static final int ACTION_BOTTOM = 8;
////                public static final int ACTION_BOTTOMRIGHT = 9;
////
////            }
//
//        private int backgroundTrackball = R.drawable.bg_trackball;
//        private int backgroundDpad = R.color.transparent;
//
//        public void setBackgroundTrackball(int backgroundTrackball) {
//            this.backgroundTrackball = backgroundTrackball;
//        }
//
//        public void setBackgroundDpad(int backgroundDpad) {
//            this.backgroundDpad = backgroundDpad;
//        }
//    }
//
////        public void changeToTrackball() {
////            if (ball == null)
////                ball = (CircleTrackball) getChildAt(1);
////            setBackgroundResource(backgroundTrackball);
////            ball.setVisibility(VISIBLE);
////            pad.setVisibility(GONE);
////            isDpad = false;
////        }
//
////        public void changeToDpad4() {
////            if (ball == null)
////                ball = (CircleTrackball) getChildAt(1);
////            pad.resizePad(getWidth() / 3);
////            setBackgroundResource(backgroundDpad);
////            ball.setVisibility(GONE);
////            pad.setVisibility(VISIBLE);
////            isDpad = true;
////        }
//
////            public boolean isDpad() {
////                return isDpad;
////            }
//
//
//

