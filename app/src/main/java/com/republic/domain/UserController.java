package com.republic.domain;

import com.republic.entities.User;
import com.republic.support.OperationCallback;

public interface UserController {

    void createUser(String name, String phone, String password, String deviceId,
                    OperationCallback operationCallBack);


    void login(String password, String userId, OperationCallback operationCallback);

    String getStoredToken();

    void updateUser(User user, String field, Object value, OperationCallback operationCallBack);

    void checkPhoneExists(String phone, OperationCallback operationCallback);

    void verifyConfirmed(String deviceId, OperationCallback operationCallBack);

    void getUserId(String deviceId, final OperationCallback operationCallback);
}
