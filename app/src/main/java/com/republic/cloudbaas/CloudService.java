package com.republic.cloudbaas;

import com.republic.entities.Corruption;
import com.republic.entities.User;
import com.republic.support.OperationCallback;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public interface CloudService {
     void addUser(User user, OperationCallback callback);
     void loginUser(User user, OperationCallback callback);
     void addCorruption(Corruption corruption, OperationCallback callback);
}
