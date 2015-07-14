package com.republic.ui.support;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/** *
 * Created by Akwasi Owusu on 7/9/15.
 */
public class Utils {

    public static class Constants{
        public static final int VIDEO_REQUEST_CODE = 28;
        public static final int CAMERA_REQUEST_CODE = 288;
        public static final String FB_ACCESS_TOKEN = "CAALW80Kj5r0BAEpfGz2ILgopKZA2z1AEJXN7FTKbuGjDY74KWh7RddOChU6jKgHkKv9ZBxeyoe2vTNdl20G8HcZAZAYOweMvTu2dUcThY39EIqZAtZBkwSsAYyiXOqJEuxdOxUPdKwTaXMAjXWwN2UfhmA216ZBKCYdMXjHQGMHCBXPUDr4aWVA";
        public static final String PAGE_PROFILE_ID = "1030146627004888";
        public static final String PAGE_FEED = "1030146627004888/feed";
        public static final String FB_APP_ID = "799290236855997";
        public static final String PAGE_VIDEOS = "1030146627004888/videos";
        public static final String PAGE_PHOTOS = "1030146627004888/photos";
        public static final String MEDIA_PATH = "media/";
        public static final String AUTHORITY = "com.republic.ui.fileprovider";
    }

    public static void switchViewVisibility(boolean visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public static void makeToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
    }
}
