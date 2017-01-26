# WaveFileGenerator
WaveFileGenerator class generates a .wav file in main dirctory for a given frequency.<br>

I have set the WaveFileGenerator class variables after going through these few links .<br>
1. http://wavefilegem.com/how_wave_files_work.html <br>
2 .http://www.topherlee.com/software/pcm-tut-wavformat.html <br>
3 .http://soundfile.sapp.org/doc/WaveFormat/ <br>

You could change the variables as per your need.<br>

There's just 1 function in the WaveFileGenerator class i.e 
```
public File getWavFile() //This returns the File object of the generated wave file.
```

If you run the app please look for "pathcheck" in your log to see the path of the generated wave file.

