package com.republic.cloudbaas;

import com.republic.entities.User;
import com.republic.support.OperationCallback;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public interface CloudService {
    void addUser(User user, OperationCallback callback);

    void loginUser(String userId, String password, OperationCallback operationCallback);

    <T> void addDataItem(T dataItem, OperationCallback callback);

    String getUserToken();
}
