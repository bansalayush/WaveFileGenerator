package com.scorpio.wavefilegenerator;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    /* EditText editFrequency;
     int frequency;*/
    WaveFileGenerator wfg;
    File f;
    Button play,stop;
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play=(Button)findViewById(R.id.buttonPlay);
        stop=(Button)findViewById(R.id.buttonStop);
        play.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);
        new FileAsyncTask().execute();
    }

    class FileAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            f = wfg.getWavFile();
            play.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wfg = new WaveFileGenerator(512);
            return null;
        }

    }

    public void onPlay(View v)
    {
        try
        {
            if(mPlayer!=null)
                mPlayer.release();

            Log.d("filpath",f.getAbsolutePath());
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(f.getAbsolutePath());
            mPlayer.setVolume(1.0f,1.0f);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setLooping(true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onStop(View v)
    {
        mPlayer.release();
    }
}
