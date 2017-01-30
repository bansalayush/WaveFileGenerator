package com.scorpio.wavefilegenerator;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Ayush Bansal on 25-01-2017.
 */

public class WaveFileGenerator {
    private File wavFile;
    private ByteBuffer bbfINT;
    private ByteBuffer bbfSHORT;

    private int sampleRate = 44100;
    private int duration = 10;
    private int bitsPerSample = 16;  private short shortBitsPerSample = 16;
    private int numberOfChannels = 2; private short shortNumberOfChannels = 2;
    private int intNumberOfSamples = duration * sampleRate;
    private int byteRate = sampleRate * numberOfChannels * bitsPerSample / 8;
    private short blockAlign = 4;/* numberOfChannels*bitsPerSample/8 */
    private int subChunk2Size = intNumberOfSamples * numberOfChannels * bitsPerSample / 8;
    private int chunkSize = 36 + subChunk2Size;
    private short PCM = 1;

    private byte[] header = new byte[44];
    private byte[] intArray = new byte[4];
    private byte[] shortArray = new byte[2];
    private byte[] soundData;

    private double samples[];
    private short buffer[];

    public WaveFileGenerator(int frequency) {

        samples = new double[intNumberOfSamples];
        buffer = new short[intNumberOfSamples];
        double note = 0.25*frequency;

        /*chunk id bigendian */
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';

        /*chunksize little endian*/
        intArray = intToByte(chunkSize, true);
        header[4] = intArray[0];
        header[5] = intArray[1];
        header[6] = intArray[2];
        header[7] = intArray[3];

        /*format big endian*/
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';

        /* subchunk1id big endian*/
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';

        /* Subchunk1Size little endian*/
        intArray = intToByte(16, true);
        header[16] = intArray[0];
        header[17] = intArray[1];
        header[18] = intArray[2];
        header[19] = intArray[3];

        /*AudioFormat little endian*/
        shortArray = shortToByte(PCM, true);
        header[20] = shortArray[0];
        header[21] = shortArray[1];

        /* NumChannels little endian*/
        shortArray = shortToByte(shortNumberOfChannels, true);
        header[22] = shortArray[0];
        header[23] = shortArray[1];

        /*SampleRate little endian*/
        intArray = intToByte(sampleRate, true);
        header[24] = intArray[0];
        header[25] = intArray[1];
        header[26] = intArray[2];
        header[27] = intArray[3];

        /* ByteRate  little endian*/
        intArray = intToByte(byteRate, true);
        header[28] = intArray[0];
        header[29] = intArray[1];
        header[30] = intArray[2];
        header[31] = intArray[3];

        /*BlockAlign little endian*/
        shortArray = shortToByte(blockAlign, true);
        header[32] = shortArray[0];
        header[33] = shortArray[1];

        /*BitsPerSample little endian*/
        shortArray = shortToByte(shortBitsPerSample, true);
        header[34] = shortArray[0];
        header[35] = shortArray[1];

        /*Subchunk2ID  bigendian*/
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';

        /*Subchunk2Size little endian*/
        intArray = intToByte(subChunk2Size, true);
        header[40] = intArray[0];
        header[41] = intArray[1];
        header[42] = intArray[2];
        header[43] = intArray[3];

        //generating frequency particular data
        for (int i = 0; i < intNumberOfSamples; ++i) {
            samples[i] = Math.sin(2 * Math.PI * i / (sampleRate / note)); // Sine wave
            buffer[i] = (short) (samples[i] * Short.MAX_VALUE);  // Higher amplitude increases volume
        }

        soundData = new byte[intNumberOfSamples];

        int i=0;
        while(i<intNumberOfSamples)
        {
            shortArray = shortToByte(buffer[i], true);//little endian
            soundData[i] = shortArray[0];
            i++;
            soundData[i] = shortArray[1];
            i++;
        }

        Log.i("soundData size",""+soundData.length);

        try {
            wavFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "wave_"+frequency+".wav");
            Log.d("pathcheck", (wavFile.getAbsolutePath()));
            if (!wavFile.exists()) {
                wavFile.createNewFile();
            }
            Log.d("writable", "" + wavFile.canWrite() + "");
            FileOutputStream os = new FileOutputStream(wavFile,true);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream outFile = new DataOutputStream(bos);
            outFile.write(header, 0,44);
            outFile.write(soundData,0,soundData.length);
            outFile.flush();
            outFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] intToByte(int i, boolean endian) {
        bbfINT = ByteBuffer.allocate(4);
        /*true for little endian*/ /*false for big endian*/
        if (endian) {
            bbfINT.order(ByteOrder.LITTLE_ENDIAN);
            return bbfINT.putInt(i).array();
        }
        bbfINT.order(ByteOrder.BIG_ENDIAN);
        return bbfINT.putInt(i).array();
    }

    private byte[] shortToByte(short i, boolean endian) {
        bbfSHORT = ByteBuffer.allocate(2);
        if (endian) {
            bbfSHORT.order(ByteOrder.LITTLE_ENDIAN);
            return bbfSHORT.putShort(i).array();
        }
        bbfSHORT.order(ByteOrder.BIG_ENDIAN);
        return bbfSHORT.putShort(i).array();
    }

    public File getWavFile()
    {
        return wavFile;
    }
}
