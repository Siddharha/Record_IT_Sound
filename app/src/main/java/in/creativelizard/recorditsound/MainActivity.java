package in.creativelizard.recorditsound;


import android.animation.PropertyValuesHolder;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OutputFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import in.creativelizard.recorditsound.util.RecorderVisualizerView;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder mRecorder;
    private String LOG_TAG = "result";
    private String mFileName;
    private RecorderVisualizerView visualizerView;
    private Handler handler;
    public static final int REPEAT_INTERVAL = 40;
    private boolean isRecording;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        UVisualizar();
    }

    private void initialize() {
        mRecorder = new MediaRecorder();
        visualizerView = (RecorderVisualizerView)findViewById(R.id.rvv);
        handler = new Handler();

        //outputFileName = "unnamed_file";
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.mp4";
       isRecording = false;
    }

    public void clkRecord(View v) {
        Toast.makeText(MainActivity.this, "Recording", Toast.LENGTH_SHORT).show();

        if ( v.getTag().equals("record")) {
            startRecording();
            ImageView imageView = (ImageView)v.findViewWithTag("record");
            imageView.setImageResource(R.drawable.ic_mic_off);
            v.setTag("stop");
        } else {
            stopRecording();
            ImageView imageView = (ImageView)v.findViewWithTag("stop");
            imageView.setImageResource(R.drawable.ic_mic);
            v.setTag("record");
        }
    }

    private void startRecording() {
        isRecording = true;
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {

            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        isRecording = false;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void UVisualizar(){
        // updates the visualizer every 50 milliseconds
        Runnable updateVisualizer = new Runnable() {
            @Override
            public void run() {
                if (isRecording) // if we are already recording
                {
                    // get the current amplitude
                    int x = mRecorder.getMaxAmplitude();
                    visualizerView.addAmplitude(x); // update the VisualizeView
                    visualizerView.invalidate(); // refresh the VisualizerView

                    // update in 40 milliseconds
                    handler.postDelayed(this, REPEAT_INTERVAL);
                }
            }
        };

    }
}
