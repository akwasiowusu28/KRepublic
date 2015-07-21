package com.republic.domain;

import com.backendless.BackendlessUser;
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

    @Override
    public void checkPhoneExists(String phone, final OperationCallback operationCallback) {
      cloudService.findUserByField(LocalConstants.PHONE, phone, new OperationCallback() {
          @Override
          public <T> void performOperation(T arg) {
               operationCallback.performOperation(arg);
          }

          @Override
          public void onOperationFailed(Throwable e) {
              super.onOperationFailed(e);
              operationCallback.performOperation(null); // return null to caller signifying phone nonexistent
          }
      });
    }

    @Override
    public void verifyConfirmed(String deviceId, final OperationCallback operationCallBack) {
        cloudService.findUserByField(LocalConstants.DEVICE_ID, deviceId, new OperationCallback() {
            @Override
            public <T> void performOperation(T arg) {
                BackendlessUser backendlessUser = (BackendlessUser)arg;
                operationCallBack.performOperation(backendlessUser.getProperty(LocalConstants.IS_CONFIRMED));
            }
        });
    }

    @Override
    public void getUserId(String deviceId, final OperationCallback operationCallback) {
        cloudService.findUserByField(LocalConstants.DEVICE_ID, deviceId, new OperationCallback() {
            @Override
            public <T> void performOperation(T arg) {
                BackendlessUser backendlessUser = (BackendlessUser)arg;
                operationCallback.performOperation(backendlessUser.getProperty(LocalConstants.OBJECT_ID));
            }

            @Override
            public void onOperationFailed(Throwable e) {
                super.onOperationFailed(e);
                operationCallback.performOperation(null); //in a failed case, return null to caller so caller can act accordingly
            }
        });
    }

    private static class LocalConstants{
        public static final String PHONE = "phone";
        public static final String IS_CONFIRMED = "isconfirmed";
        public static final String OBJECT_ID = "objectId";
        public static final String DEVICE_ID = "deviceid";
    }
}
