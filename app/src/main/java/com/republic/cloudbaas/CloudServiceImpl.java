package com.republic.cloudbaas;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
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


    @Override
    public <T> void initialize(T dataContext) {
        Backendless.initApp(dataContext, LocalConstants.APPLICATION_ID,
                LocalConstants.SECRET_KEY, LocalConstants.VERSION);
    }

    public void addUser(User user, OperationCallback callback) {

    }

    public void loginUser(String userId, String password, OperationCallback operationCallback) {

        isLogin = true;

        Backendless.UserService.login(userId, password, new BackendAsyncCallBack<BackendlessUser>(operationCallback), true);
    }


    @Override
    public <T> void addDataItem(T dataItem, OperationCallback callback) {

    }

    @Override
    public String getUserToken() {
        return UserTokenStorageFactory.instance().getStorage().get();
    }

    private class BackendAsyncCallBack<T> implements AsyncCallback<T> {

        private OperationCallback callback;

        public BackendAsyncCallBack(OperationCallback callback) {
            this.callback = callback;
        }

        @Override
        public void handleResponse(T response) {
            if (isLogin) {

                String result = getUserToken();
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

    private class LocalConstants{
        public static final String APPLICATION_ID = "E7F75A25-A1E0-CC13-FF5B-111FBA427400";
        public static final String SECRET_KEY = "7F9A514F-01DF-409D-FF72-3F1E8C9AFC00";
        public static final String VERSION = "v1";
    }
}
