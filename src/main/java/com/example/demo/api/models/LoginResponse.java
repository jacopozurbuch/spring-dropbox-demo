package com.example.demo.api.models;

public class LoginResponse {

    private String message;
    private boolean state;

    public LoginResponse(String message, boolean state) {
        this.message = message;
        this.state = state;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isState() {
        return state;
    }

    
    
}
