package com.republic.cloudbaas;

import com.backendless.BackendlessUser;
import com.republic.entities.User;
import com.republic.support.OperationCallback;

/**
 * Created by aowusu on 7/28/2015.
 */
public interface CloudUserService {

    void addUser(User user, OperationCallback<BackendlessUser> operationCallBack) ;

    void updateUser(User user, OperationCallback<BackendlessUser> operationCallBack) ;
    void login(String userId, String password, OperationCallback<BackendlessUser> operationCallBack) ;

    String getUserToken();
}
