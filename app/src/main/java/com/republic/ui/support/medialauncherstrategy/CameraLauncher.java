package com.republic.ui.support.medialauncherstrategy;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.republic.ui.support.Utils;

import java.io.File;

/**
 * Created by Akwasi Owusu on 7/16/15.
 */

public class CameraLauncher implements Launcher{

    private Fragment fragment;

    public CameraLauncher(Fragment fragment){
        super();
        this.fragment = fragment;
    }
    @Override
    public String launch() {
        String photoFileName = getPhotoFileName() ;
        Uri photoUri = Uri.fromFile(new File(photoFileName));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        fragment.startActivityForResult(intent, Utils.Constants.CAMERA_REQUEST_CODE);

        return photoFileName;
    }

    private String getPhotoFileName() {
        File mediaDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Utils.Constants.MEDIA_PATH);
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        return mediaDir.getPath() + Utils.makeFileName();
    }
}
