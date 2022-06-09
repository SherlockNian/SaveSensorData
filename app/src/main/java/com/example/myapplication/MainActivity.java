package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements
        OnClickListener {

    private static final int DEFAULT_SAMPLE_RESOLUTION = 500;
    private Button btnStart, btnStop;
    private boolean started = false;
    private TextView tvState;
    private EditText etSampleResolution;


    Intent intent;
    private int sampleResolution;
    private int m_sessionId;
    private SharedPreferences m_pref;
    private Editor m_prefEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitGUIItems();
        intent = new Intent(getApplicationContext(), SensorSampleService.class );
        InitSharedPreferences();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(started)
            StopClick();
    }

    //need to do the override here because m_cameraHandler is not an activity and cannot get the result

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                StartClick();
                break;
            case R.id.btnStop:
                StopClick();
                break;
            default:
                break;
        }

    }

    private void StartClick(){
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        sampleResolution = Integer.parseInt(etSampleResolution.getText().toString());
        started = true;
        intent.putExtra("sessionId", GetSessionId());
        intent.putExtra("startListening", true);
        intent.putExtra("sampleResolution", sampleResolution);
        startService(intent);
        tvState.setText("Started");
    }

    private void StopClick(){
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        started = false;
        intent.putExtra("startListening", false);
        startService(intent);
        IncrementSessionId();
        tvState.setText("Stopped");
    }

    private void InitGUIItems() {
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        tvState = (TextView) findViewById((R.id.tvState));
        etSampleResolution = (EditText) findViewById(R.id.etSampleResolution);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        sampleResolution = DEFAULT_SAMPLE_RESOLUTION;
        etSampleResolution.setText(String.valueOf(sampleResolution));
    }

    private void InitSharedPreferences(){
        m_pref = getApplicationContext().getSharedPreferences("SensorRecorderPref", 0);
        m_prefEditor = m_pref.edit();
    }

    private int GetSessionId(){
        return m_pref.getInt("sessionId", 0);
    }

    private void IncrementSessionId(){
        int currentValue = GetSessionId();
        currentValue++;
        m_prefEditor.putInt("sessionId", currentValue);
        m_prefEditor.commit();
    }

}