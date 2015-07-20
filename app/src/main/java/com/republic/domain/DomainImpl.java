package com.republic.domain;

import com.republic.support.RepublicFactory;

/**
 * Created by aowusu on 7/20/2015.
 */
public class DomainImpl implements Domain {

    @Override
    public <T> void initialize(T context) {
        RepublicFactory.getCloudService().initialize(context);
    }
}
