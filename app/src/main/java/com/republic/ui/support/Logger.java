package com.republic.ui.support;

import android.util.Log;

/**
 * Created by Akwasi Owusu on 7/22/15.
 */
public class Logger {

    public static void log(Class klass, String message){
        Log.e(klass.getSimpleName(), message);
    }
}
