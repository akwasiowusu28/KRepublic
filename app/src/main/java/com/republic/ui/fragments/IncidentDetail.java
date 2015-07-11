package com.republic.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.republic.ui.R;
import com.republic.ui.support.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class IncidentDetail extends Fragment {

    public static IncidentDetail newInstance() {
        IncidentDetail fragment = new IncidentDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private MediaRecorder audioRecorder;
    private String fileName;
    private ImageButton voiceButton;
    private Button recordingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_incident_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields();
        setupButtons();
    }


    private void setupButtons() {

        View view = getView();
        if (view != null) {
            ImageButton videoButton = (ImageButton) view.findViewById(R.id.videoButton);
            videoButton.setOnClickListener(buttonClickListener);

            voiceButton = (ImageButton) view.findViewById(R.id.voiceButton);
            voiceButton.setOnClickListener(buttonClickListener);

            recordingButton = (Button) view.findViewById(R.id.recordingButton);
            recordingButton.setOnClickListener(buttonClickListener);

            ImageButton cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
            cameraButton.setOnClickListener(buttonClickListener);

            ImageButton galleryButton = (ImageButton) view.findViewById(R.id.galleryButton);
            galleryButton.setOnClickListener(buttonClickListener);

            Button submitButton = (Button)view.findViewById(R.id.submitButton);
            submitButton.setOnClickListener(buttonClickListener);
        }
    }

    private OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.videoButton:
                    launchVideoRecorder();
                    break;
                case R.id.cameraButton:
                    launchCamera();
                    break;
                case R.id.voiceButton:
                    launchAudioRecorder();
                    break;
                case R.id.submitButton:
                    submit();
                    break;
            }
        }
    };

    private void submit() {
        AccessToken accessToken = new AccessToken(Utils.Constants.FB_ACCESS_TOKEN,
                Utils.Constants.FB_APP_ID,
                Utils.Constants.PAGE_PROFILE_ID,
                null,null,
                AccessTokenSource.FACEBOOK_APPLICATION_WEB
                ,null,null);

        Bundle params = new Bundle();
        params.putString("message","Akua is awesome!");
        GraphRequest request = new GraphRequest(accessToken,
                Utils.Constants.PAGE_FEED,params, HttpMethod.POST, new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse graphResponse) {

            }
        });

        request.executeAsync();
    }

    private void initializeFields() {
        audioRecorder = new MediaRecorder();
    }

    private void launchVideoRecorder() {

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, 28);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Utils.Constants.VIDEO_REQUEST_CODE:
                    break;
                case Utils.Constants.CAMERA_REQUEST_CODE:
                    break;
            }
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, 288);
    }

    private void prepareAudioRecorder() {
        fileName = getActivity().getCacheDir().getAbsolutePath() + "/"
                + String.valueOf(Math.random() * 10) + ".awb";
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        audioRecorder.setOutputFile(fileName);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            audioRecorder.prepare();
        } catch (IOException e) {
            //TODO: log it
        }
    }

    private void toggleRecordingButtonsVisibility(boolean isRecording){
        Utils.switchViewVisibility(!isRecording, voiceButton);
        Utils.switchViewVisibility(isRecording, recordingButton);
    }
    private void launchAudioRecorder() {
        prepareAudioRecorder();
        toggleRecordingButtonsVisibility(true);
        audioRecorder.start();


        new CountDownTimer(30000,1000){
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

    }

    private void disposeAudioRecorder(){
        if(audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
       disposeAudioRecorder();
    }
}
