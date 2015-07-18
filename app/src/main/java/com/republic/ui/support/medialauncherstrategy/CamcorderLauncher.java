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

public class CamcorderLauncher implements Launcher {

    private Fragment fragment;

    public CamcorderLauncher(Fragment fragment) {
        super();
        this.fragment = fragment;
    }

    @Override
    public String launch() {
        String videoFileName = getVideoFileName();
        Uri videoFileUri = Uri.fromFile(new File(videoFileName));

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        fragment.startActivityForResult(intent, Utils.Constants.VIDEO_REQUEST_CODE);

        return videoFileName;
    }

    private String getVideoFileName() {
        File mediaDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Utils.Constants.MEDIA_PATH);
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        return mediaDir.getPath() + Utils.makeFileName();
    }
}
