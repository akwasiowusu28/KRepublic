package com.republic.ui.support.posterstrategy;

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

/**
 * Created by Akwasi Owusu on 7/27/15.
 */
public class PlainTextPoster implements Poster{
    @Override
    public void post(Context context, final Corruption corruption, AccessToken accessToken, final OperationCallback<Integer> callback) {
        Bundle params = new Bundle();
        params.putString(Utils.Constants.MESSAGE, Utils.getNarrative(context, corruption));
        GraphRequest request = new GraphRequest(accessToken,
                Utils.Constants.PAGE_FEED, params, HttpMethod.POST, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                if (graphResponse.getError() == null) {
                    try {
                        String post_Id = graphResponse.getJSONObject().getString("id");
                        corruption.setPostId(post_Id);
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
