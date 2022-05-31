package com.example.myapplication;

public class GyroData extends SensorData {
    public GyroData(int sessionId, long timestamp, double x, double y, double z) {
        super(sessionId, timestamp, x, y, z, "Gyro");
    }
}
