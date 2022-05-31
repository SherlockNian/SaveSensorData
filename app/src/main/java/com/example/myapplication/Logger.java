package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Environment;



import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


    public class Logger {

    File csvPath;
    CSVWriter csvWriter;
    public SensorData data;


    public void InitCSVWriter(int sessionId){
        try{
            @SuppressLint("DefaultLocale") String sessionFolder = String.format("Session_%d" , sessionId);
            File folder = new File(Environment.getExternalStorageDirectory() + "/myapplication/save/" + sessionFolder);//make this common to Logger and camera Handler
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                csvPath = folder;
                File file = new File(csvPath, "/myapplication/save/"+"_"+sessionFolder+".csv");
                if(csvPath.canWrite()) {
                    FileWriter writer = new FileWriter(file, true);
                    csvWriter = new CSVWriter(writer);
                }
            }  // Do something else on failure


            if (csvWriter == null)
                throw new Exception("null");
        }
        catch(java.io.IOException e) {

            //dostuff

        }
        catch(Exception e) {
            //do stuff
        }

    }

    public void CloseCSVFile(){
        if( csvWriter != null) {
            csvWriter.close();
        }
    }
///
    public void RecordDataToFile(SensorData data){
    String time, x, y, z, sensorName;
    time =
    x =
    y =
    z =
    sensorName = data.GetSensorName();
    String[] dataToWrite = (new String[]{time, sensorName, x, y, z});
    if( csvWriter != null) {
        csvWriter.writeNext(dataToWrite);
    }
}
    public void RecordEventToFile(String eventToLog) {
        String[] dataToWrite = (new String[]{String.valueOf(System.currentTimeMillis()), eventToLog});
        if( csvWriter != null) {
            csvWriter.writeNext(dataToWrite);
        }
    }
}