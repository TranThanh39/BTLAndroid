package com.haruma.app.utility;

import com.haruma.app.model.User;

public class UserSessionManager {
    private static UserSessionManager instance;
    private User currentUser;

    private UserSessionManager() {
    }

    public static synchronized UserSessionManager getInstance() {
        if (instance == null) {
            instance = new UserSessionManager();
        }
        return instance;
    }

    public void saveUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }
}

