package com.scorpio.wavefilegenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    EditText editFrequency;
    int frequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editFrequency = (EditText)findViewById(R.id.editFrequency);
        WaveFileGenerator wfg = new WaveFileGenerator(512);
        File f = wfg.getWavFile();
    }
}
