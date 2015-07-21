package com.republic.cloudbaas;

import com.republic.entities.User;
import com.republic.support.OperationCallback;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public interface CloudService {

    public <T> void initialize(T dataContext);

    void addUser(User user, OperationCallback callback);

    void loginUser(String userId, String password, OperationCallback operationCallback);

    void findUserByField(String fieldName, String searchValue, OperationCallback operationCallback);

    String getUserToken();
}
