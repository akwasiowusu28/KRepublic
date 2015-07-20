package com.republic.support;

import com.republic.cloudbaas.CloudServiceImpl;
import com.republic.cloudbaas.CloudService;
import com.republic.domain.Domain;
import com.republic.domain.DomainImpl;
import com.republic.domain.UserController;
import com.republic.domain.UserControllerImpl;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class RepublicFactory {

    public static CloudService getCloudService(){
        return new CloudServiceImpl();
    }

    public static UserController getUserController() {
        return new UserControllerImpl();
    }

    public static Domain getDomain(){
        return new DomainImpl();
    }
}
