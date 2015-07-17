package com.republic.ui.support.launchingstrategy;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.republic.support.ArgumentValidator;
import com.republic.ui.R;
import com.republic.ui.support.Utils;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class MediaFileSystemLauncher<T> implements Launcher<T> {

    private Context context;
    private ImageButton galleryButton;
    private Fragment fragment;

    public MediaFileSystemLauncher(Fragment fragment){
        super();
        new ArgumentValidator().VerifyParamsNonNull(fragment);
        this.fragment = fragment;
        this.context = fragment.getActivity();
        View view = fragment.getView();
        if(view != null) {
            galleryButton = (ImageButton) fragment.getView().findViewById(R.id.galleryButton);
        }
    }

    @Override
    public T launch() {
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

        return null;
    }

    private void browse(int selectedGalleryItem) {
        Intent intent = null;
        int requestCode = -1;

        switch (selectedGalleryItem) {
            case R.id.action_photo:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                requestCode = Utils.Constants.GALLERY_PHOTO_REQUEST;
                break;
            case R.id.action_video:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                requestCode = Utils.Constants.GALLERY_VIDEO_REQUEST;
                break;
            case R.id.action_audio:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                requestCode = Utils.Constants.GALLERY_AUDIO_REQUEST;
                break;
        }
        if (intent != null)
            fragment.startActivityForResult(intent, requestCode);
    }
}
