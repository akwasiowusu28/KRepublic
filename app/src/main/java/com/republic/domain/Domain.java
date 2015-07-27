package com.republic.domain;

import com.republic.entities.Corruption;
import com.republic.support.OperationCallback;

/**
 * Created by aowusu on 7/20/2015.
 */
public interface Domain {

    <T> void initialize( T context);
    void saveCorruption(Corruption corruption);
    void loadAllUserCorruptions(String userId, OperationCallback<Corruption> callback);
}
