package com.example.demo.api.models;

public class UserInit {

    private User user;
    private String passwd;

    public void User(User user, String passwd) {
        this.user = user;
        this.passwd = passwd;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public String getPasswd() {
        return passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    
}
