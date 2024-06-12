package com.goride.models;

public class User {
    private int userId;
    private String username;
    private String email;
    private long phoneNumber;
    private String password;

    public User(int userId, String username, String email, long phoneNumber, String password) {
        this(username, email, phoneNumber, password);
        this.userId = userId;
    }

    public User(String username, String email, long phoneNumber, String password){
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
