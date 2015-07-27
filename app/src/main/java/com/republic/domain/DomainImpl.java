package com.republic.domain;

import com.republic.cloudbaas.CloudService;
import com.republic.entities.Corruption;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;

/**
 * Created by Akwasi Owusu on 7/20/2015.
 */
public class DomainImpl implements Domain {

    CloudService cloudService;

    public DomainImpl() {
        this.cloudService = RepublicFactory.getCloudService();
    }

    @Override
    public <T> void initialize(T context) {
        cloudService.initialize(context);
    }

    @Override
    public void saveCorruption(Corruption corruption) {
        cloudService.addDataItem(corruption, Corruption.class, null); //fire and forget for now
    }

    @Override
    public void loadAllUserCorruptions(String userId, OperationCallback<Corruption> callback) {
         cloudService.findItemsByFieldName(LocalConstants.OWNER_ID, userId, Corruption.class, callback);
    }

    private class LocalConstants{
        public static final String OWNER_ID = "ownerId";
    }

}