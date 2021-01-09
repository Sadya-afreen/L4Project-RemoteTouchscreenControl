package com.example.android_motionsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Gyroscope {
    public interface Listener{ // interface for observer design pattern
        void onRotation(float rx, float ry, float rz);   // these are the rotations about the x,y and z axis
    }

    private Listener listener; // instance of listener

    public void setListener(Listener l){
        listener = l;
    }
   private SensorManager sensorManager;
   private Sensor sensor;
   private SensorEventListener sensorEventListener;

   Gyroscope(Context context){
       sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
       sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
       sensorEventListener=new SensorEventListener() {
           @Override
           public void onSensorChanged(SensorEvent event) {
               if(listener!=null){
                   listener.onRotation(event.values[0], event.values[1],event.values[2]);
               }
           }

           @Override
           public void onAccuracyChanged(Sensor sensor, int accuracy) {

           }
       };
   }
    public void register() {
        sensorManager.registerListener(sensorEventListener,sensor,sensorManager.SENSOR_DELAY_NORMAL);
    }// register sensor notification

    public void unregister(){
        sensorManager.unregisterListener(sensorEventListener);
    }//unregister from sensor notification
}
