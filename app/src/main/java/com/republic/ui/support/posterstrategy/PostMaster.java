package com.republic.ui.support.posterstrategy;

import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.republic.entities.Corruption;
import com.republic.ui.support.Utils;

/** *
 * Created by Akwasi Owusu on 7/14/15.
 */
public class PostMaster {

    private AccessToken accessToken;
    private Poster poster;
    private Context context;

    public PostMaster(Context context, Poster poster){
        super();
        this.poster = poster;
        this.context = context;

        accessToken = new AccessToken(Utils.Constants.FB_ACCESS_TOKEN,
                Utils.Constants.FB_APP_ID,
                Utils.Constants.PAGE_PROFILE_ID,
                null, null,
                AccessTokenSource.FACEBOOK_APPLICATION_WEB
                , null, null);
    }

    public void post(Corruption corruption) {
       poster.post(context, corruption, accessToken);
    }
}
