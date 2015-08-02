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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.republic.entities.Corruption;
import com.republic.entities.CorruptionType;
import com.republic.entities.MediaType;
import com.republic.support.OperationCallback;
import com.republic.ui.R;
import com.republic.ui.support.Logger;
import com.republic.ui.support.Utils;
import com.republic.ui.support.medialauncherstrategy.AudioRecordLauncher;
import com.republic.ui.support.medialauncherstrategy.CamcorderLauncher;
import com.republic.ui.support.medialauncherstrategy.CameraLauncher;
import com.republic.ui.support.medialauncherstrategy.LaunchMaster;
import com.republic.ui.support.medialauncherstrategy.MediaFileSystemLauncher;
import com.republic.ui.support.posterstrategy.AudioPoster;
import com.republic.ui.support.posterstrategy.PhotoPoster;
import com.republic.ui.support.posterstrategy.PlainTextPoster;
import com.republic.ui.support.posterstrategy.PostMaster;
import com.republic.ui.support.posterstrategy.Poster;
import com.republic.ui.support.posterstrategy.VideoPoster;

public class IncidentDetail extends Fragment {

    public static IncidentDetail newInstance() {
        IncidentDetail fragment = new IncidentDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private class LocalConstants {
        public static final String USER_NULL = "User id is null";
        public static final String CORRUPTION_NULL = "corruption is null";
    }

    private String audioFileName;
    private String photoFileName;
    String videoFileName;
    private Context context;
    private AudioRecordLauncher audioRecordLauncher;
    private MediaType selectedMediaType = MediaType.NONE;
    private String userId = null;
    CheckBox honorCheckBox;
    EditText locationField;
    EditText descriptionField;
    private ViewGroup.LayoutParams descriptionLayoutParam = null;

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
        audioRecordLauncher = new AudioRecordLauncher(this);
        userId = Utils.readFromPref(context, Utils.Constants.USER_TOKEN);
        View view = getView();
        boolean areValid = false;
        if (view != null) {
            honorCheckBox = (CheckBox) view.findViewById(R.id.honorCheck);
            locationField = (EditText) view.findViewById(R.id.location);
            descriptionField = (EditText) view.findViewById(R.id.incidentDescription);
            descriptionLayoutParam = descriptionField.getLayoutParams();
        }
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
                    videoFileName = new LaunchMaster(new CamcorderLauncher(IncidentDetail.this)).launch();
                    selectedMediaType = MediaType.VIDEO;
                    break;
                case R.id.cameraButton:
                    photoFileName = new LaunchMaster(new CameraLauncher(IncidentDetail.this)).launch();
                    selectedMediaType = MediaType.PHOTO;
                    break;
                case R.id.voiceButton:
                    audioFileName = new LaunchMaster(audioRecordLauncher).launch();
                    selectedMediaType = MediaType.AUDIO;
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

        if (areFieldsValid()) {
            switch (selectedMediaType) {
                case VIDEO:
                    postCorruption(videoFileName, new VideoPoster());
                    break;
                case PHOTO:
                    postCorruption(photoFileName, new PhotoPoster());
                    break;
                case AUDIO:
                    postCorruption(audioFileName, new AudioPoster());
                    break;
                default:
                    postCorruption(Utils.Constants.EMPTY_STRING, new PlainTextPoster());
                    break;
            }
        }

    }

    private boolean areFieldsValid() {
        View view = getView();
        boolean areValid = false;

        Utils.switchInvalidFieldsBackColor(true, locationField, descriptionField);
        descriptionField.setLayoutParams(descriptionLayoutParam);

        if (!isAnyEditFieldEmpty(locationField, descriptionField)) {
            if (honorCheckBox.isChecked()) {
                areValid = true;
            } else {
                Utils.makeToast(context, R.string.checkHonor);
            }
        }
        return areValid;
    }

    private boolean isAnyEditFieldEmpty(EditText... fields) {
        boolean isAnyEmpty = false;
        for (EditText field : fields) {
            if (field.getText().toString().trim().equals(Utils.Constants.EMPTY_STRING)) {
                isAnyEmpty = true;
                Utils.switchInvalidFieldsBackColor(false, field);
                break;
            }
        }
        return isAnyEmpty;
    }

    private void postCorruption(String fileName, Poster poster) {
        Corruption corruption = buildCorruptionObject(fileName);

        if (corruption != null) {
            new PostMaster(context, poster).post(corruption, new OperationCallback<Corruption>() {
                @Override
                public void performOperation(Corruption arg) {
                    String postId = arg.getPostId();
                    Utils.launchFacebookPage(context,
                            Utils.Constants.FB_APP_POST+postId,
                            Utils.Constants.FB_DOT_COM + postId);
                }
            });
            reset();
        } else {
            Logger.log(IncidentDetail.class, LocalConstants.CORRUPTION_NULL);
        }
    }

    private Corruption buildCorruptionObject(String fileName) {
        Corruption corruption = null;
        if (userId != null) {
            corruption = new Corruption();
            corruption.setMediaFilePath(fileName);

            View view = getView();

            if (view != null) {
                String location = ((EditText) view.findViewById(R.id.location)).getText().toString();
                String description = ((EditText) view.findViewById(R.id.incidentDescription)).getText().toString();
                corruption.setLocation(location);
                corruption.setDescription(description);
                corruption.setMediaType(selectedMediaType);
            }
            corruption.setCorruptionType(getSelectedCorruptionType());
            corruption.setOwnerId(userId);
        } else {
            Logger.log(this.getClass(), LocalConstants.USER_NULL);
        }
        return corruption;
    }

    private CorruptionType getSelectedCorruptionType() {
        Bundle args = this.getArguments();

        return (CorruptionType) args.getSerializable(Utils.Constants.SELECTED_CORRUPTION_TYPE);
    }

    private void reset(){
        selectedMediaType = MediaType.NONE;
        Utils.clearInputTextFields(locationField, descriptionField);
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
                        selectedMediaType = MediaType.PHOTO;
                    }
                    break;
                case Utils.Constants.GALLERY_VIDEO_REQUEST:
                    if (data != null) {
                        videoFileName = retrieveFilePathFromUri(data.getData(), MediaStore.Video.Media.DATA);
                        selectedMediaType = MediaType.VIDEO;
                    }
                    break;
                case Utils.Constants.GALLERY_AUDIO_REQUEST:
                    if (data != null) {
                        audioFileName = retrieveFilePathFromUri(data.getData(), MediaStore.Images.Media.DATA);
                        selectedMediaType = MediaType.AUDIO;
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
