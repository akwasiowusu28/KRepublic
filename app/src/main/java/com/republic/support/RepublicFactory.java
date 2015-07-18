package com.republic.support;

import com.republic.cloudbaas.CloudAccess;
import com.republic.cloudbaas.CloudService;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class RepublicFactory {

    public static CloudService getCloudService(){
        return new CloudAccess();
    }
}
