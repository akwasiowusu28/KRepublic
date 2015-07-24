package com.republic.ui.support.posterstrategy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.republic.entities.Corruption;
import com.republic.support.OperationCallback;
import com.republic.ui.R;
import com.republic.ui.support.Logger;
import com.republic.ui.support.Utils;

import org.json.JSONException;

import java.io.File;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */

public class VideoPoster implements Poster {

    @Override
    public void post(final Context context, final Corruption corruption, final AccessToken accessToken, final OperationCallback<Integer> callback) {
        File file = new File(corruption.getMediaFilePath());

        Bundle params = new Bundle();
        params.putString("description", Utils.getNarrative(context, corruption));
        params.putByteArray(corruption.getMediaFilePath(), Utils.convertFileToBytes(file));

        GraphRequest request = new GraphRequest(accessToken,
                Utils.Constants.PAGE_VIDEOS, params, HttpMethod.POST, new GraphRequest.Callback() {
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
}
