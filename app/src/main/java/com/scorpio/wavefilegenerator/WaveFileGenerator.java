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
    File wavFile;
    ByteBuffer bbfINT;
    ByteBuffer bbfSHORT;
    int sampleRate = 22050;
    int duration = 5;
    int bitsPerSample = 16;
    int numberOfChannels = 1;
    short shortNumberOfChannels = 2;
    short shortBitsPerSample = 16;
    int intNumberOfSamples = duration * sampleRate;
    int byteRate = sampleRate * numberOfChannels * bitsPerSample / 8;
    short blockAlign = 16;
    int subChunk2Size = intNumberOfSamples * numberOfChannels * bitsPerSample / 8;
    int chunkSize = 28 + subChunk2Size;
    short PCM = 1;
    byte[] header = new byte[44];
    byte[] intArray = new byte[4];
    byte[] shortArray = new byte[2];

    public WaveFileGenerator(int frequency) {

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

        try {
            wavFile = new File(Environment.getExternalStoragePublicDirectory((String)Environment.DIRECTORY_MUSIC), "headerWaveFile.wav");
            Log.d("pathcheck", (Environment.getExternalStorageDirectory().toString() + "/headerWaveFile.wav"));
            if (!wavFile.exists()) {
                wavFile.createNewFile();
            }
            Log.d("writable","" + wavFile.canWrite() + "");
            FileOutputStream os = new FileOutputStream(wavFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream outFile = new DataOutputStream(bos);
            outFile.write(header, 0, 44);
            outFile.flush();
            outFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] intToByte(int i, boolean endian) {
        bbfINT = ByteBuffer.allocate(4);
        /*true for little endian*/ /*false for big endian*/
        if (endian) {
            bbfINT.order(ByteOrder.LITTLE_ENDIAN);
            return bbfINT.putInt(i).array();
        }
        bbfINT.order(ByteOrder.BIG_ENDIAN);
        return bbfINT.putInt(i).array();
    }

    public byte[] shortToByte(short i, boolean endian) {
        bbfSHORT = ByteBuffer.allocate(2);
        if (endian) {
            bbfSHORT.order(ByteOrder.LITTLE_ENDIAN);
            return bbfSHORT.putShort(i).array();
        }
        bbfSHORT.order(ByteOrder.BIG_ENDIAN);
        return bbfSHORT.putShort(i).array();
    }
}
