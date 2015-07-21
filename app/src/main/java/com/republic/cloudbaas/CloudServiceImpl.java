package com.republic.cloudbaas;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.exceptions.CloudRequestFailedException;

import java.util.List;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class CloudServiceImpl implements CloudService {

    private boolean isLogin = false;
    private boolean isCollectionOperation;

    //This is to make sure the right objects are passed to the callback method in case there are multiple request that return collections.
    // Increment this any time you run a request that has a potential of returning a collection and  a collection is what you want
    private int collectionRequestsCount = 0;

    @Override
    public <T> void initialize(T dataContext) {
        Backendless.initApp(dataContext, LocalConstants.APPLICATION_ID,
                LocalConstants.SECRET_KEY, LocalConstants.VERSION);
    }

    public void addUser(User user, OperationCallback callback) {
        Backendless.UserService.register(user, new BackendAsyncCallBack<BackendlessUser>(callback));
    }

    public void loginUser(String userId, String password, OperationCallback operationCallback) {

        isLogin = true;

        Backendless.UserService.login(userId, password, new BackendAsyncCallBack<BackendlessUser>(operationCallback), true);
    }

    @Override
    public void findUserByField(String fieldName, String searchValue, OperationCallback operationCallback) {

        String whereClause = fieldName + "='" + searchValue + "'";
        BackendlessDataQuery query = new BackendlessDataQuery(whereClause);
        Backendless.Persistence
                .of(BackendlessUser.class)
                .find(query,
                        new BackendAsyncCallBack<BackendlessCollection<BackendlessUser>>(operationCallback));
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
                if (callback != null)
                    if (response instanceof BackendlessCollection) {
                        List<T> responseData = ((BackendlessCollection<T>) response)
                                .getData();
                        if (!isCollectionOperation) {
                            T item = responseData.size() > 0 ? responseData.get(0)
                                    : null;
                            callback.performOperation(item);
                        } else {
                            collectionRequestsCount--;
                            callback
                                    .performOperation(responseData);
                        }
                    } else {
                        callback.performOperation(response);
                    }
                isCollectionOperation = collectionRequestsCount > 0 && isCollectionOperation;
            }
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            if (isLogin && fault.getCode().equals(LocalConstants.LOGIN_FAILED_CODE)) {
                callback.performOperation(LocalConstants.EMPTY_STRING); // return and empty string (token) if login failed
                isLogin = false;
            }
            callback.onOperationFailed(new CloudRequestFailedException(fault.getMessage()));
        }
    }

    private class LocalConstants {
        public static final String APPLICATION_ID = "E7F75A25-A1E0-CC13-FF5B-111FBA427400";
        public static final String SECRET_KEY = "7F9A514F-01DF-409D-FF72-3F1E8C9AFC00";
        public static final String VERSION = "v1";
        public static final String LOGIN_FAILED_CODE = "3003";
        public static final String EMPTY_STRING = "";
    }
}
