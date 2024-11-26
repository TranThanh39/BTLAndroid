package com.haruma.app.model;

public class UserDetail {
    private int userId;
    private String fullName;
    private String className;
    private String phoneNumber;
    private User user;

    public UserDetail(int userId, String fullName, String className, String phoneNumber) {
        this.userId = userId;
        this.fullName = fullName;
        this.className = className;
        this.phoneNumber = phoneNumber;
    }

    public UserDetail(String fullName, String className, String phoneNumber) {
        this.fullName = fullName;
        this.className = className;
        this.phoneNumber = phoneNumber;
    }

    public UserDetail() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

