package com.republic.ui.support.posterstrategy;

import android.app.ProgressDialog;
import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.republic.domain.Domain;
import com.republic.entities.Corruption;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;
import com.republic.ui.R;
import com.republic.ui.support.Utils;

/**
 * Created by Akwasi Owusu on 7/14/15.
 */
public class PostMaster {

    private AccessToken accessToken;
    private Poster poster;
    private Context context;
    private ProgressDialog progressDialog;
    private Domain domain;

    public PostMaster(Context context, Poster poster) {
        super();
        this.poster = poster;
        this.context = context;

        domain = RepublicFactory.getDomain();

        accessToken = new AccessToken(Utils.Constants.FB_ACCESS_TOKEN,
                Utils.Constants.FB_APP_ID,
                Utils.Constants.PAGE_PROFILE_ID,
                null, null,
                AccessTokenSource.FACEBOOK_APPLICATION_WEB
                , null, null);
        setupProgressDialog();
    }

    public void post(final Corruption corruption, final OperationCallback<Corruption> callback) {
        progressDialog.show();
        poster.post(context, corruption, accessToken, new OperationCallback<Integer>() {
            @Override
            public void performOperation(Integer messageStringId) {
                domain.saveCorruption(corruption);
                dismissProgressDialog();

                callback.performOperation(messageStringId == R.string.success ? corruption : (Corruption)null);

                Utils.makeToast(context.getApplicationContext(), messageStringId);
            }
        });
    }

    private void setupProgressDialog() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(context.getText(R.string.posting));
    }


    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
