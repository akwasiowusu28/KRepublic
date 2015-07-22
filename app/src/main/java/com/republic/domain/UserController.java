package com.republic.domain;

import com.republic.entities.User;
import com.republic.support.OperationCallback;

public interface UserController {

    void createUser(String name, String phone, String password, String deviceId,
                    OperationCallback<User> operationCallBack);


    void findUser(String deviceId, OperationCallback<User> operationCallback);

    void login(String password, String userId, OperationCallback<User> operationCallback);

    void updateUser(User user, OperationCallback<User> operationCallBack);

    void checkPhoneExists(String phone, OperationCallback<User> operationCallback);

    void verifyConfirmed(String deviceId, OperationCallback<User> operationCallBack);

  //  void getUserId(String deviceId, final OperationCallback operationCallback);
}
