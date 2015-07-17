package com.republic.ui.support.launchingstrategy;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */

public class CameraLauncher<T> implements Launcher<T>{

    private Fragment fragment;

    public CameraLauncher(Fragment fragment){
        super();
        this.fragment = fragment;
    }
    @Override
    public T launch() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        fragment.startActivityForResult(intent, 288);

        return null;
    }
}
