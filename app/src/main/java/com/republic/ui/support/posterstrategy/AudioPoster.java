package com.republic.ui.support.posterstrategy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.republic.entities.Corruption;
import com.republic.ui.R;
import com.republic.ui.support.OperationCallback;
import com.republic.ui.support.Utils;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class AudioPoster implements Poster {

    @Override
    public void post(final Context context, final Corruption corruption, final AccessToken accessToken) {
        postAudioFileToSoundCloud(context, corruption.getMediaFilePath(), new OperationCallback() {

            @Override
            public <T> void performOperation(T audioLink) {
                Bundle params = new Bundle();
                params.putString("message", audioLink + "\n" + corruption.getDescription());
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

    private static void postAudioFileToSoundCloud(final Context context, final String audioFileName, final OperationCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

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

                final String link = soundCloud.postTrack(new Track(audioFileName, audioFileName)).getPermalinkUrl();

                new Handler(context.getMainLooper()).post(
                        new Runnable() {
                            @Override
                            public void run() {
                                callback.performOperation(link);
                            }
                        });
            }
        }).start();
    }
}
