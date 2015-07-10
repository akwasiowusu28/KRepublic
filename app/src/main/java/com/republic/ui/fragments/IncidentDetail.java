package com.republic.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.republic.ui.R;
import com.republic.ui.support.Utils;


public class IncidentDetail extends Fragment {

    public static IncidentDetail newInstance() {
        IncidentDetail fragment = new IncidentDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_incident_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       ImageButton videoButton = (ImageButton)view.findViewById(R.id.videoButton);
        videoButton.setOnClickListener(buttonClickListener);

        ImageButton voiceButton = (ImageButton)view.findViewById(R.id.voiceButton);
        voiceButton.setOnClickListener(buttonClickListener);

        ImageButton cameraButton = (ImageButton)view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(buttonClickListener);

        ImageButton galleryButton = (ImageButton)view.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(buttonClickListener);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.videoButton:
                    launchVideoRecorder();
                    break;
                case R.id.cameraButton:
                    launchCamera();
            }
        }
    };

    private void launchVideoRecorder(){

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, 28);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK){
             switch (requestCode){
                 case Utils.Constants.VIDEO_REQUEST_CODE:
                     break;
                 case Utils.Constants.CAMERA_REQUEST_CODE:
                     break;
             }
        }
    }

    private void launchCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        startActivityForResult(intent, 288);
    }
}
