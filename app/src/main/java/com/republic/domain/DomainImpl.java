package com.republic.domain;

import com.republic.cloudbaas.CloudPersistenceService;
import com.republic.cloudbaas.CloudQueryService;
import com.republic.entities.Corruption;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;

/**
 * Created by Akwasi Owusu on 7/20/2015.
 */
public class DomainImpl implements Domain {

    CloudPersistenceService cloudPersistenceService;
    CloudQueryService cloudQueryService;

    public DomainImpl() {
        this.cloudPersistenceService = RepublicFactory.getPersistenceService();
        this.cloudQueryService = RepublicFactory.getQueryService();
    }

    @Override
    public <T> void initialize(T context) {
        cloudPersistenceService.initialize(context);
    }

    @Override
    public void saveCorruption(Corruption corruption) {
        cloudPersistenceService.addDataItem(corruption, Corruption.class, null); //fire and forget for now
    }

    @Override
    public void loadAllUserCorruptions(String userId, OperationCallback<Corruption> callback) {
         cloudQueryService.findItemsByFieldName(LocalConstants.OWNER_ID, userId, Corruption.class, callback);
    }

    private class LocalConstants{
        public static final String OWNER_ID = "ownerId";
    }

}