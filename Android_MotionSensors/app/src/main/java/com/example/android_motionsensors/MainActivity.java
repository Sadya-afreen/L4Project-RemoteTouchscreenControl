package com.example.android_motionsensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity implements SensorEventListener {
      private SensorManager sensorManager;
   // private Accelerometer accelerometer;
   // private Gyroscope gyroscope;
    Socket s;
    PrintWriter out;

    private boolean isConnected = false;
    private boolean mouseMoved = false;
    protected static boolean mRunning = false;
    SocketService mBoundService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBoundService = ((SocketService.LocalBinder)service).getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            mBoundService = null;
        }
    };
    private void doBindService() {
        bindService(new Intent(MainActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final String hostname = getIntent().getStringExtra("IP");
        startService(new Intent(MainActivity.this,SocketService.class).putExtra("IP",hostname));
        doBindService();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

         Button leftClick = (Button) findViewById(R.id.leftClick);
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

        Button rightClick = (Button) findViewById(R.id.rightClick);
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
//        accelerometer = new Accelerometer(this);
//      //  gyroscope = new Gyroscope(this);
//
//        accelerometer.setListener(new Accelerometer.Listener() {
//            @Override
//            public void onTransalation(float tx, float ty, float tz) {
//                if(tx>1.0f){
//                    getWindow().getDecorView().setBackgroundColor(Color.RED);
//                }
//                else if(tx<-1.0f){
//                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
//
//                }
//
//            }
//        });
//
//        gyroscope.setListener(new Gyroscope.Listener() {
//            @Override
//            public void onRotation(float rx, float ry, float rz) {
//                if(rz>1.0f){
//                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
//                }
//                else if(rz<-1.0f){
//                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
//
//                }
//            }
//        });


//    @Override
//    protected void onPause() {
//        super.onPause();
//        accelerometer.unregister();
//      //  gyroscope.unregister();
//    }

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
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }


    }
    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
        if(mBoundService!=null){
            Log.i("accel", x+" "+y+" "+z);
            mBoundService.sendMessage((x +"," +y+","+z ).toString());
        }

       // Log.i("accel", x+" "+y+" "+z);

    }
//    public void leftClick(View v)
//    {
//        Log.i("leftclick","");
//        if(mBoundService!=null){
//
//            mBoundService.sendMessage(("leftclick"));
//        }
//    }
//
//    public void leftrelease(View v){
//        if(mBoundService!=null){
//
//            mBoundService.sendMessage(("leftrelease"));
//        }
//    }

//    public void rightClick(View v)
//    {
//        Log.i("rightclick","");
//        mBoundService.sendMessage(("rightclick"));
//    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        // accelerometer.register();
        //  gyroscope.register();
    }

    @Override
    protected void onPause(){
        sensorManager.unregisterListener(this);
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}
