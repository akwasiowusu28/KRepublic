package com.republic.cloudbaas;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.republic.entities.Corruption;
import com.republic.entities.User;
import com.republic.support.OperationCallback;
import com.republic.support.exceptions.CloudRequestFailedException;

/**
 * Created by Akwasi Owusu on 7/18/15.
 */
public class CloudAccess implements CloudService{

    public void addUser(User user, OperationCallback callback){

    }

    public void loginUser(User user, OperationCallback callback){

    }

    @Override
    public void addCorruption(Corruption corruption, OperationCallback callback) {

    }

    private class BackendAsyncCallBack<T> implements AsyncCallback<T> {

        private OperationCallback callback;

        public BackendAsyncCallBack(OperationCallback callback){
            this.callback = callback;
        }

        @Override
        public void handleResponse(T response) {
           callback.performOperation(response);
        }

        @Override
        public void handleFault(BackendlessFault fault) {
           callback.onOperationFailed(new CloudRequestFailedException(fault.getMessage()));
        }
    }
}
