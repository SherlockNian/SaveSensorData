package com.example.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import java.io.File;


public class SensorSampleService extends Service implements SensorEventListener {
    private static final String DEBUG_TAG = "SensorSampleService";

    public SensorManager sensorManager ;

    public long m_accelTimeStamp, m_gyroTimeStamp;

    private Logger m_log;
    private int m_sampleResolution;
    private int m_sessionId;

    private boolean m_serviceStarted;

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        m_log = new Logger();
        m_serviceStarted = false;

        //register Intent filters
        IntentFilter filter = new IntentFilter();
        filter.addAction("CameraHandler.PictureTaken");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Decide if we starting or ending the service
        boolean startListening = intent.getExtras().getBoolean("startListening");
        m_sessionId = intent.getExtras().getInt("sessionId", 0);

        if (startListening) {
            Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accel,
                    SensorManager.SENSOR_DELAY_FASTEST);
            Sensor gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_FASTEST);

            m_accelTimeStamp = System.currentTimeMillis();
            m_gyroTimeStamp = System.currentTimeMillis();

            m_log.InitCSVWriter(m_sessionId);

            m_sampleResolution = intent.getExtras().getInt("sampleResolution");
            if (m_sampleResolution < 50)
                m_sampleResolution = 500;

            m_serviceStarted = true;

        } else {
            sensorManager.unregisterListener(this);
            m_log.CloseCSVFile();
            m_serviceStarted = false;
            stopSelf();
        }

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (m_serviceStarted) {
            int sensorType = event.sensor.getType();
            switch (sensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    HandleAccel(event);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    HandleGyro(event);
                    break;
                default:
                    break;
            }
        }

    }

    private void HandleAccel(SensorEvent event) {

        AccelData data = new AccelData(m_sessionId, System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
        if (data.getTimestamp() > m_accelTimeStamp + m_sampleResolution) {
            //save data to file
            m_log.RecordDataToFile(data);
            m_accelTimeStamp = data.getTimestamp();
        }
    }

        private void HandleGyro(SensorEvent event) {

            GyroData data = new GyroData(m_sessionId, System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            if (data.getTimestamp() > m_gyroTimeStamp + m_sampleResolution) {
                //save data to file
                m_log.RecordDataToFile(data);
                m_gyroTimeStamp = data.getTimestamp();
            }
        }
            private final BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                }
            };

}
