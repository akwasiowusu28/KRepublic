package com.republic.ui.support;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.republic.entities.Corruption;
import com.republic.ui.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

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
        private static final String MP4 = ".mp4";
        public static final int GALLERY_PHOTO_REQUEST = 2888;
        public static final int GALLERY_VIDEO_REQUEST = 28888;
        public static final int GALLERY_AUDIO_REQUEST = 288888;
        public static final String SELECTED_CORRUPTION_TYPE = "SelectedCorruptionType";
        public static final String PREF_FILE = "com.republic.RepublicPREF";
        public static final String EMPTY_STRING = "";
        public static final String USER_CONFIRMED = "User confirmed";
        public static final String USER_TOKEN = "User token";
        public static final String PHONE ="phone";
    }

    private static Drawable defaultEditTextBackground = null;

    public static void switchViewVisibility(boolean visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public static void makeToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
    }

    public static String makeFileName() {
        return  File.separator + String.valueOf(Calendar.getInstance().getTimeInMillis()) + Constants.MP4;
    }

    public static byte[] convertFileToBytes(File file) {
        byte[] fileBytes = null;

        try {
            fileBytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBytes;
    }

    public static boolean isEmptyString(String value){
        return value == null || value.isEmpty();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public static void switchInvalidFieldsBackColor(boolean isValid, EditText... fields) {
        if (defaultEditTextBackground == null && fields.length > 0) {
            defaultEditTextBackground = fields[0].getBackground();
        }
        if (isValid) {
            for (EditText field : fields) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    field.setBackground(defaultEditTextBackground);
                } else {
                    field.setBackgroundResource(android.R.drawable.edit_text);
                }
            }
        } else {
            for (EditText field : fields) {
                field.setBackgroundColor(Color.rgb(250, 213, 182));
            }
        }
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static void writeToPref(Context context, String key, String value){
        SharedPreferences sharedPreference = context.getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String readFromPref(Context context, String key){
        SharedPreferences sharedPreference = context.getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
        return sharedPreference.getString(key,Constants.EMPTY_STRING);
    }

    public static String getNarrative(Context context, Corruption corruption){
        String token = readFromPref(context, Constants.USER_TOKEN);

        String citizenId = token.substring(token.length() - 5);
        String citizen = "Citizen " + citizenId;
        String narrative = citizen + " says: \n\n" + context.getString(R.string.opening_line) + " ";
        narrative += corruption.getCorruptionType().toString() + " ";
        narrative += context.getString(R.string.at).toLowerCase() + " " + corruption.getLocation() + ".\n\n";
        narrative += context.getString(R.string.incident_description) + ":\n\n";
        narrative += corruption.getDescription() + "\n\n";
        narrative += "This " + corruption.getMediaType().toString().toLowerCase() + " " + context.getString(R.string.this_is_evidence) + "\n \n";
        narrative += context.getString(R.string.honor_code);

        return narrative;
    }
}
