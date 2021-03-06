package com.project.nicki.displaystabilizer.dataprovider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.project.nicki.displaystabilizer.dataprocessor.proAccelerometer;

/**
 * Created by nicki on 10/27/2015.
 */
public class getAccelerometer implements Runnable {
    public static float AcceX, AcceY, AcceZ;
    String ACCEBROADCAST_STRING = "ACCEBROADCAST";
    private String TAG = "getAccelerometer";
    private Context mContext;
    private SensorManager mSensorManager = null;
    private Sensor mSensor;
    private SensorEventListener mListener;
    private HandlerThread mHandlerThread;
    private Runnable mRunnable = new proAccelerometer(mContext);
    public getAccelerometer(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mHandlerThread = new HandlerThread("AccelerometerLogListener");
        mHandlerThread.start();
        Handler handler = new Handler(mHandlerThread.getLooper());
        mListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                AcceX = event.values[0];
                AcceY = event.values[1];
                AcceZ = event.values[2];
                mRunnable.run();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST,
                handler);
    }
}
