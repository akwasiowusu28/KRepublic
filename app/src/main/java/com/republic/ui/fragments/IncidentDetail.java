package com.republic.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.republic.ui.R;
import com.republic.ui.support.PostHelper;
import com.republic.ui.support.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class IncidentDetail extends Fragment {

    private static class LocalConstants{
        public static final int GALLERY_PHOTO_REQUEST = 2888;
        public static final int GALLERY_VIDEO_REQUEST = 28888;
        public static final int GALLERY_AUDIO_REQUEST = 288888;
    }

    public static IncidentDetail newInstance() {
        IncidentDetail fragment = new IncidentDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private MediaRecorder audioRecorder;
    private String audioFileName;
    private ImageButton voiceButton;
    private Button recordingButton;
    String videoFileName;
    private Context context;
    private String imageFileName;
    ImageButton galleryButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_incident_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupButtons();

        initializeFields();
    }

    private void initializeFields() {
        context = getActivity();
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

            galleryButton = (ImageButton) view.findViewById(R.id.galleryButton);
            galleryButton.setOnClickListener(buttonClickListener);

            Button submitButton = (Button) view.findViewById(R.id.submitButton);
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
                case R.id.galleryButton:
                    openMediaFilesOnSystem();
                    break;
            }
        }
    };

    private void submit() {

        PostHelper.postAudioLinkToFaceBook(context, audioFileName);
    }


    private void openMediaFilesOnSystem() {
        // Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.)
        PopupMenu popupMenu = new PopupMenu(context, galleryButton);
        popupMenu.getMenuInflater().inflate(R.menu.menu_gallery, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                browse(menuItem.getItemId());
                return true;
            }
        });

        popupMenu.show();
    }


    private void browse(int selectedGalleryItem) {
        Intent intent = null;
        int requestCode = -1;

        switch (selectedGalleryItem) {
            case R.id.action_photo:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                requestCode = LocalConstants.GALLERY_PHOTO_REQUEST;
                break;
            case R.id.action_video:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                requestCode = LocalConstants.GALLERY_VIDEO_REQUEST;
                break;
            case R.id.action_audio:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                requestCode = LocalConstants.GALLERY_AUDIO_REQUEST;
                break;
        }
        if (intent != null)
            startActivityForResult(intent, requestCode);
    }

    private void launchVideoRecorder() {
        Uri videoFileUri = getVideoFileUri();

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, Utils.Constants.VIDEO_REQUEST_CODE);
    }

    private Uri getVideoFileUri() {
        File mediaDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Utils.Constants.MEDIA_PATH);
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        videoFileName = mediaDir.getPath() + Utils.makeFileName();
        File videoFile = new File(videoFileName);

        return Uri.fromFile(videoFile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Utils.Constants.VIDEO_REQUEST_CODE:
                    break; //TODO doing nothing yet
                case Utils.Constants.CAMERA_REQUEST_CODE:
                    break; //TODO doing nothing yet
            }
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, 288);
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

    private void toggleRecordingButtonsVisibility(boolean isRecording) {
        Utils.switchViewVisibility(!isRecording, voiceButton);
        Utils.switchViewVisibility(isRecording, recordingButton);
    }

    private void launchAudioRecorder() {
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

    }

    private void disposeAudioRecorder() {
        if (audioRecorder != null) {
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
