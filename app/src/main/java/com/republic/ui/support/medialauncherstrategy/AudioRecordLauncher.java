package com.republic.ui.support.medialauncherstrategy;

import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageButton;

import com.republic.ui.R;
import com.republic.ui.support.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class AudioRecordLauncher implements Launcher{

    private Button recordingButton;
    private ImageButton voiceButton;
    private MediaRecorder audioRecorder;
    private String audioFileName;


    public AudioRecordLauncher(Fragment fragment){
        super();
        recordingButton = (Button)fragment.getView().findViewById(R.id.recordingButton);
        voiceButton = (ImageButton)fragment.getView().findViewById(R.id.voiceButton);
    }

    @Override
    public String launch() {

        prepareAudioRecorder();
        toggleRecordingButtonsVisibility(true);
        audioRecorder.start();


        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long remaining) {
                String s = String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(remaining),
                        TimeUnit.MILLISECONDS.toSeconds(remaining));
                recordingButton.setText(s);

            }

            @Override
            public void onFinish() {

                recordingButton.setCompoundDrawables(null, null, null, null);
                recordingButton.setText("Done!");
                disposeAudioRecorder();
            }
        }.start();

        return audioFileName;
    }

    private void toggleRecordingButtonsVisibility(boolean isRecording) {
        Utils.switchViewVisibility(!isRecording, voiceButton);
        Utils.switchViewVisibility(isRecording, recordingButton);
    }

    private void prepareAudioRecorder() {
        File mediaDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), Utils.Constants.MEDIA_PATH);
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        audioFileName = mediaDir.getPath() + Utils.makeFileName();

        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        audioRecorder.setOutputFile(audioFileName);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            audioRecorder.prepare();
        } catch (IOException e) {
            //TODO: log it
        }
    }

    public void disposeAudioRecorder() {
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
        }
    }
}
