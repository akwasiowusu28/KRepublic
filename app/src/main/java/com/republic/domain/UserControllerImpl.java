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
    public void createUser(String name, String phone, String password, String deviceId, final OperationCallback<User> operationCallBack) {
        User user = new User(name, password, phone, deviceId, false);
        RepublicFactory.getSession().setUser(user);
        cloudService.addUser(user, new OperationCallback<BackendlessUser>() {
            @Override
            public void performOperation(BackendlessUser arg) {
                operationCallBack.performOperation(constructUserFromBackendless(arg));
            }

            @Override
            public void onOperationFailed(Throwable e) {
                super.onOperationFailed(e);
                operationCallBack.performOperation((User)null);
            }
        });
    }

    @Override
    public void findUser(String deviceId, final OperationCallback<User> operationCallback) {
        cloudService.findItemByFieldName(LocalConstants.DEVICE_ID, deviceId, BackendlessUser.class,
                new OperationCallback<BackendlessUser>() {
            @Override
            public void performOperation(BackendlessUser arg) {
                User user = constructUserFromBackendless(arg);
                RepublicFactory.getSession().setUser(user);
                operationCallback.performOperation(user);
            }

            @Override
            public void onOperationFailed(Throwable e) {
                super.onOperationFailed(e);
                operationCallback.performOperation((User)null);
            }
        });
    }

    @Override
    public void login(String userId, String password, final OperationCallback<User> operationCallback) {

        cloudService.login(userId, password, new OperationCallback<BackendlessUser>() {
            @Override
            public void performOperation(BackendlessUser arg) {
                User user = constructUserFromBackendless(arg);
                RepublicFactory.getSession().setUser(user);
                operationCallback.performOperation(user);
            }

            @Override
            public void onOperationFailed(Throwable e) {
                super.onOperationFailed(e);
                operationCallback.performOperation((User)null);
            }
        });
    }

    @Override
    public void updateUser(User user, final OperationCallback<User> operationCallBack) {
        cloudService.updateUser(user, new OperationCallback<BackendlessUser>() {
            @Override
            public void performOperation(BackendlessUser arg) {
                operationCallBack.performOperation(constructUserFromBackendless(arg));
            }

            @Override
            public void onOperationFailed(Throwable e) {
                operationCallBack.onOperationFailed(e);
            }
        });
    }

    @Override
    public void checkPhoneExists(String phone, final OperationCallback<User> operationCallback) {

        cloudService.findItemsByFieldName(LocalConstants.PHONE, phone, User.class, new OperationCallback<User>() {
            @Override
            public void performOperation(User arg) {
                super.performOperation(arg);
            }

            @Override
            public void onOperationFailed(Throwable e) {
                super.onOperationFailed(e);
                operationCallback.performOperation((User)null); // return null to caller signifying phone nonexistent
            }
        });
    }

    private User constructUserFromBackendless(BackendlessUser backendlessUser) {
        User user = null;

        if (backendlessUser != null) {
            String objectId = backendlessUser.getProperty(LocalConstants.OBJECT_ID).toString();
            String name = backendlessUser.getProperty(LocalConstants.NAME).toString();
            String phone = backendlessUser.getProperty(LocalConstants.PHONE).toString();
            String deviceId = backendlessUser.getProperty(LocalConstants.DEVICE_ID).toString();
            boolean isConfirmed = Boolean.parseBoolean(backendlessUser.getProperty(LocalConstants.IS_CONFIRMED).toString());

            user = new User(name, null, phone, deviceId, isConfirmed);
            user.setObjectId(objectId);
            user.setProperties(backendlessUser.getProperties());

        }
        return user;
    }

    private static class LocalConstants{
        public static final String PHONE = "phone";
        public static final String IS_CONFIRMED = "isconfirmed";
        public static final String OBJECT_ID = "objectId";
        public static final String NAME = "name";
        public static final String DEVICE_ID = "deviceid";
    }
}
