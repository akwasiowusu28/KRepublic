package com.republic.ui.support.posterstrategy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.republic.entities.Corruption;
import com.republic.ui.R;
import com.republic.support.OperationCallback;
import com.republic.ui.support.Logger;
import com.republic.ui.support.Utils;

import org.json.JSONException;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class AudioPoster implements Poster {

    @Override
    public void post(final Context context, final Corruption corruption, final AccessToken accessToken, final OperationCallback<Integer> callback) {
        postAudioFileToSoundCloud(context, corruption.getMediaFilePath(), new OperationCallback<String>() {

            @Override
            public void performOperation( String audioLink) {
                Bundle params = new Bundle();
                params.putString("message", audioLink + "\n" + Utils.getNarrative(context, corruption));
                GraphRequest request = new GraphRequest(accessToken,
                        Utils.Constants.PAGE_FEED, params, HttpMethod.POST, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        if (graphResponse.getError() == null) {
                            try {
                                corruption.setPostId(graphResponse.getJSONObject().getString("post_id"));
                            } catch (JSONException e) {
                                Logger.log(AudioPoster.class, e.getMessage());
                            }
                            callback.performOperation(R.string.success);
                        } else {
                            callback.performOperation(R.string.failed);
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
