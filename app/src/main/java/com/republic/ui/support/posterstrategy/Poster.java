package com.republic.ui.support.posterstrategy;

import android.content.Context;

import com.facebook.AccessToken;
import com.republic.entities.Corruption;

/** *
 * Created by Akwasi Owusu on 7/16/15.
 */
public interface Poster {

    void post(Context context, Corruption corruption, AccessToken accessToken);

}
