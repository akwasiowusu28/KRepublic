package com.republic.domain;

import com.republic.entities.User;

/**
 * Created by Akwasi Owusu on 7/21/15.
 */
public class SessionImpl implements Session {

    private static Session instance;
    private User user;

    private SessionImpl(){
        super();
    }

    public static Session getInstance(){
        if(instance == null){
            synchronized (Session.class) { // using the intrinsic lock of the Class instance associated with Session because instance is static
                if(instance == null){
                    instance = new SessionImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getCurrentUser() {
        return user;
    }
}
