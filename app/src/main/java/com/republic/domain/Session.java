package com.republic.domain;

import com.republic.entities.User;

/**
 * Created by Akwasi Owusu on 7/21/15.
 */
public interface Session {

    void setUser(User user);

    User getCurrentUser();
}
