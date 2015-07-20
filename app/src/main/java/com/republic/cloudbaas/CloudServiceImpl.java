package com.republic.cloudbaas;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.exceptions.CloudRequestFailedException;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class CloudServiceImpl implements CloudService {

    private boolean isLogin = false;

    public void addUser(User user, OperationCallback callback) {

    }

    public void loginUser(String userId, String password, OperationCallback operationCallback) {

        isLogin = true;

        Backendless.UserService.login(userId, password, new BackendAsyncCallBack<>(operationCallback), true);
    }


    @Override
    public <T> void addDataItem(T dataItem, OperationCallback callback) {

    }

    @Override
    public String getUserToken() {
        return null;
    }

    private class BackendAsyncCallBack<T> implements AsyncCallback<T> {

        private OperationCallback callback;

        public BackendAsyncCallBack(OperationCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleResponse(T response) {
            if (isLogin) {

                String result = UserTokenStorageFactory.instance().getStorage().get();
                callback.performOperation(result);
                isLogin = false;

            } else {
                callback.performOperation(response);
            }
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            callback.onOperationFailed(new CloudRequestFailedException(fault.getMessage()));
        }
    }
}
