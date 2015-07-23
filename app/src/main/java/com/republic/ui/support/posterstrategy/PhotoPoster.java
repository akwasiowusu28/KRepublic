package com.republic.ui.support.posterstrategy;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.republic.entities.Corruption;
import com.republic.ui.R;
import com.republic.ui.support.Utils;

import java.io.File;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public class PhotoPoster implements Poster{
    @Override
    public void post(final Context context, final Corruption corruption, final AccessToken accessToken) {
        File file = new File(corruption.getMediaFilePath());
        Bundle params = new Bundle();
        params.putString("caption", Utils.getNarrative(context, corruption));
        params.putByteArray(corruption.getMediaFilePath(), Utils.convertFileToBytes(file));

        GraphRequest request = new GraphRequest(accessToken,
                Utils.Constants.PAGE_PHOTOS, params, HttpMethod.POST, new GraphRequest.Callback() {
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
}
