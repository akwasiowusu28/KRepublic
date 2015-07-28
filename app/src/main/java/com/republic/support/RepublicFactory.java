package com.republic.support;

import com.republic.cloudbaas.CloudQueryService;
import com.republic.cloudbaas.CloudServiceImpl;
import com.republic.cloudbaas.CloudPersistenceService;
import com.republic.cloudbaas.CloudUserService;
import com.republic.domain.Domain;
import com.republic.domain.DomainImpl;
import com.republic.domain.Session;
import com.republic.domain.SessionImpl;
import com.republic.domain.UserController;
import com.republic.domain.UserControllerImpl;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
// A better setup should be considered
public class RepublicFactory {

    public static CloudPersistenceService getPersistenceService(){
        return new CloudServiceImpl();
    }

    public static CloudQueryService getQueryService(){
        return new CloudServiceImpl();
    }

    public static CloudUserService getUserService(){
        return new CloudServiceImpl();
    }

    public static UserController getUserController() {
        return new UserControllerImpl();
    }

    public static Domain getDomain(){
        return new DomainImpl();
    }

    public static Session getSession(){
        return SessionImpl.getInstance();
    }
}
