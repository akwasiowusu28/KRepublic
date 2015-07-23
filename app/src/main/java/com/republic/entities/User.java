package com.republic.entities;

import com.backendless.BackendlessUser;

/**
 * Created by Akwasi Owusu on 7/17/15.
 */
public class User extends BackendlessUser {
    private String objectId;
    private boolean isConfirmed;
    private String password;
    private String name;
    private String deviceId;
    private String phone;

    public User() {
        super();
    }

    public User(String name, String password, String phone, String deviceId, boolean isConfirmed) {
        super();
        this.password = password;
        this.name = name;
        this.deviceId = deviceId;
        this.phone = phone;
        this.isConfirmed = isConfirmed;

        setProperty(LocalConstants.NAME, name);
        setProperty(LocalConstants.PASSWORD, password);
        setProperty(LocalConstants.PHONE, phone);
        setProperty(LocalConstants.DEVICE_ID, deviceId);
        setProperty(LocalConstants.IS_CONFIRMED, isConfirmed);
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        setProperty(LocalConstants.IS_CONFIRMED, isConfirmed);
        this.isConfirmed = isConfirmed;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        setProperty(LocalConstants.PASSWORD, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        setProperty(LocalConstants.DEVICE_ID, deviceId);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private class LocalConstants {
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String DEVICE_ID = "deviceid";
        public static final String IS_CONFIRMED = "isconfirmed";
        public static final String PHONE = "phone";

    }

    @Override
    public String toString() {
        return name;
    }
}
