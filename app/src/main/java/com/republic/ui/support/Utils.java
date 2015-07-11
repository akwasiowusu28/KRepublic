package com.republic.ui.support;

import android.view.View;

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
    }

    public static void switchViewVisibility(boolean visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}
