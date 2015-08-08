package com.republic.ui.support.medialauncherstrategy;

import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.republic.ui.R;
import com.republic.ui.support.Logger;
import com.republic.ui.support.Utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class AudioRecordLauncher implements Launcher{

    private Button recordingButton;
    private ImageButton voiceButton;
    private MediaRecorder audioRecorder;
    private String audioFileName;
    private CountDownTimer countDownTimer;

    public AudioRecordLauncher(Fragment fragment){
        super();
        if(fragment != null){
            View view = fragment.getView();
            if(view != null){
                recordingButton = (Button)view.findViewById(R.id.recordingButton);
                voiceButton = (ImageButton)view.findViewById(R.id.voiceButton);
            }
        }

    }

    @Override
    public String launch() {

        prepareAudioRecorder();
        toggleRecordingButtonsVisibility(true);
        audioRecorder.start();


        countDownTimer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long remaining) {

                long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) - TimeUnit.MINUTES.toSeconds(minutes);

                String s = String.format("%d : %02d",
                        minutes,
                        seconds);
                recordingButton.setText(s);

            }

            @Override
            public void onFinish() {

                recordingButton.setCompoundDrawables(null, null, null, null); //TODO: DRY
                recordingButton.setText("Done!");
                disposeAudioRecorder();
            }
        }.start();

        return audioFileName;
    }

    public void stopRecording(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        disposeAudioRecorder();
        recordingButton.setCompoundDrawables(null, null, null, null); //TODO: DRY
        recordingButton.setText("Done!");
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
        } catch (Exception e) {
            Logger.log(AudioRecordLauncher.class, e.getMessage());
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
