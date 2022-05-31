package com.example.myapplication;

public class AccelData extends SensorData {

        public AccelData(int sessionId, long timestamp, double x, double y, double z){
            super(sessionId, timestamp, x, y, z, "Accel");
        }
}
