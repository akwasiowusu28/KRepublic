package com.republic.domain;

import com.republic.cloudbaas.CloudService;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.RepublicFactory;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class UserControllerImpl implements UserController {

    private CloudService cloudService;

    public UserControllerImpl() {
        super();
        cloudService = RepublicFactory.getCloudService();
    }

    @Override
    public void createUser(String name, String phone, String password, String deviceId, OperationCallback operationCallBack) {
        User user = new User(name, password, phone, deviceId, false);
        cloudService.addUser(user, operationCallBack);
    }

    @Override
    public void login(String userId, String password, OperationCallback operationCallback) {
         cloudService.loginUser(userId,password, operationCallback);
    }

    @Override
    public String getStoredToken() {
        return cloudService.getUserToken();
    }

    @Override
    public void updateUser(User user, String field, Object value, OperationCallback operationCallBack) {

    }
}
