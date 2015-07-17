package com.republic.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.republic.ui.R;
import com.republic.ui.support.Utils;
import com.republic.ui.support.launchingstrategy.AudioRecordLauncher;
import com.republic.ui.support.launchingstrategy.CamcorderLauncher;
import com.republic.ui.support.launchingstrategy.CameraLauncher;
import com.republic.ui.support.launchingstrategy.LaunchMaster;
import com.republic.ui.support.launchingstrategy.MediaFileSystemLauncher;
import com.republic.ui.support.postingstrategy.AudioPoster;
import com.republic.ui.support.postingstrategy.PhotoPoster;
import com.republic.ui.support.postingstrategy.PostMaster;
import com.republic.ui.support.postingstrategy.VideoPoster;

public class IncidentDetail extends Fragment {

    private enum SelectedMediaType {
        VIDEO, PHOTO, AUDIO
    }

    public static IncidentDetail newInstance() {
        IncidentDetail fragment = new IncidentDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private String audioFileName;
    private String photoFileName;
    String videoFileName;
    private Context context;
    private AudioRecordLauncher audioRecordLauncher;
    private SelectedMediaType selectedMediaType;

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
        audioRecordLauncher = new AudioRecordLauncher<String>(this);
    }

    private void setupButtons() {

        View view = getView();
        if (view != null) {
            ImageButton videoButton = (ImageButton) view.findViewById(R.id.videoButton);
            videoButton.setOnClickListener(buttonClickListener);

            ImageButton voiceButton = (ImageButton) view.findViewById(R.id.voiceButton);
            voiceButton.setOnClickListener(buttonClickListener);

            Button recordingButton = (Button) view.findViewById(R.id.recordingButton);
            recordingButton.setOnClickListener(buttonClickListener);

            ImageButton cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
            cameraButton.setOnClickListener(buttonClickListener);

            ImageButton galleryButton = (ImageButton) view.findViewById(R.id.galleryButton);
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
                    videoFileName = new LaunchMaster<String>(new CamcorderLauncher<String>(IncidentDetail.this)).launch();
                    selectedMediaType = SelectedMediaType.VIDEO;
                    break;
                case R.id.cameraButton:
                    photoFileName = new LaunchMaster<String>(new CameraLauncher<String>(IncidentDetail.this)).launch();
                    selectedMediaType = SelectedMediaType.PHOTO;
                    break;
                case R.id.voiceButton:
                    audioFileName = new LaunchMaster<String>(audioRecordLauncher).launch();
                    selectedMediaType = SelectedMediaType.AUDIO;
                    break;
                case R.id.galleryButton:
                    new LaunchMaster(new MediaFileSystemLauncher(IncidentDetail.this)).launch();
                    break;
                case R.id.submitButton:
                    submit();
                    break;
            }
        }
    };

    private void submit() {

        switch (selectedMediaType){
            case VIDEO:
                new PostMaster(context, new VideoPoster()).post(videoFileName);
                break;
            case PHOTO:
                new PostMaster(context, new PhotoPoster()).post(photoFileName);
                break;
            case AUDIO:
                new PostMaster(context, new AudioPoster()).post(audioFileName);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Utils.Constants.VIDEO_REQUEST_CODE:
                    break; //TODO doing nothing yet
                case Utils.Constants.CAMERA_REQUEST_CODE:
                    break; //TODO doing nothing yet
                case Utils.Constants.GALLERY_PHOTO_REQUEST:
                    if (data != null) {
                        photoFileName = retrieveFilePathFromUri(data.getData(), MediaStore.Images.Media.DATA);
                        selectedMediaType = SelectedMediaType.PHOTO;
                    }
                    break;
                case Utils.Constants.GALLERY_VIDEO_REQUEST:
                    if (data != null) {
                        videoFileName = retrieveFilePathFromUri(data.getData(), MediaStore.Video.Media.DATA);
                        selectedMediaType = SelectedMediaType.VIDEO;
                    }
                    break;
                case Utils.Constants.GALLERY_AUDIO_REQUEST:
                    if (data != null) {
                        audioFileName = retrieveFilePathFromUri(data.getData(), MediaStore.Images.Media.DATA);
                        selectedMediaType = SelectedMediaType.AUDIO;
                    }
                    break;
            }
        }
    }

    private String retrieveFilePathFromUri(Uri data, String projectionClause) {

        String[] projection = {projectionClause};

        Cursor cursor = context.getContentResolver().query(data,
                projection, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);

        cursor.close();

        return filePath;
    }

    @Override
    public void onPause() {
        super.onPause();
        audioRecordLauncher.disposeAudioRecorder();
    }
}
