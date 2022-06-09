package com.example.myapplication;


public class SensorData {
    public int sessionId;
    public long timestamp;
    private double x;
    private double y;
    private double z;
    private String sensorName;

    public SensorData(int sessionId, long timestamp, double x, double y, double z, String sensorName) {
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.sensorName=sensorName;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public String toString()
    {
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }

    public String GetSensorName(){
        return sensorName;
    }

    public String GetSessionId(){
        return String.valueOf(sessionId);}
}
