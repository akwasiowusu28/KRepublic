package com.republic.ui.support;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.republic.ui.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;

/** *
 * Created by Akwasi Owusu on 7/14/15.
 */
public class PostHelper {

    private static AccessToken accessToken = new AccessToken(Utils.Constants.FB_ACCESS_TOKEN,
            Utils.Constants.FB_APP_ID,
            Utils.Constants.PAGE_PROFILE_ID,
            null, null,
            AccessTokenSource.FACEBOOK_APPLICATION_WEB
            , null, null);

    private void postPictureToFaceBook(final Context context, String videoFileName) {
        File file = new File(videoFileName);
        Bundle params = new Bundle();

        params.putByteArray(videoFileName, convertFileToBytes(file));
        GraphRequest request = new GraphRequest(accessToken,
                Utils.Constants.PAGE_VIDEOS, params, HttpMethod.POST, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError() != null) {
                    Utils.makeToast(context, R.string.failed);
                } else {
                    Utils.makeToast(context, R.string.success);
                }
            }
        });

        request.executeAsync();
    }

    public static void postAudioLinkToFaceBook(final Context context, String audioFileName) {
        postAudioFileToSoundCloud(context, audioFileName, new OperationCallback(){

            @Override
            public <T> void performOperation(T audioLink) {
                Bundle params = new Bundle();
                params.putString("message", (String)audioLink);
                GraphRequest request = new GraphRequest(accessToken,
                        Utils.Constants.PAGE_FEED, params, HttpMethod.POST, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        if (graphResponse.getError() != null) {
                            Utils.makeToast(context, R.string.failed);
                        } else {
                            Utils.makeToast(context, R.string.success);
                        }
                    }
                });

                request.executeAsync();

        }

        });
    }

    public static void postVideoToFaceBook(final Context context, String videoFileName) {

        File file = new File(videoFileName);
        Bundle params = new Bundle();

        params.putByteArray(videoFileName, convertFileToBytes(file));
        GraphRequest request = new GraphRequest(accessToken,
                Utils.Constants.PAGE_VIDEOS, params, HttpMethod.POST, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError() != null) {
                    Utils.makeToast(context, R.string.failed);
                } else {
                    Utils.makeToast(context, R.string.success);
                }
            }
        });

        request.executeAsync();
    }

    private static byte[] convertFileToBytes(File file) {
        byte[] fileBytes = null;

        try {
            fileBytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBytes;
    }

    private static void postAudioFileToSoundCloud(final Context context, final String audioFileName, final OperationCallback callback) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SoundCloud soundCloud = new SoundCloud
                                (
                                        context.getString(R.string.soundcloud_client_id),
                                        context.getString(R.string.soundcloud_client_secret)
                                );

                        soundCloud.login
                                (
                                        context.getString(R.string.soundcloud_login_id),
                                        context.getString(R.string.soundcloud_pword)
                                );

                        final String link = soundCloud.postTrack(new Track(audioFileName, audioFileName)).getPermalink();

                        new Handler(context.getMainLooper()).post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.performOperation(link);
                                    }
                                });
                    } catch (Exception e) { //Bad practice. I know, move on!
                        //TODO log it
                    }
                }
            }).start();
    }
}
