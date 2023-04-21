package com.example.demo.api.models;

public class LoginResponse {

    private String message;
    private boolean state;
    private String jwtToken;

    public LoginResponse(String message, boolean state) {
        this.message = message;
        this.state = state;
    }

    public LoginResponse(String message, boolean state, String jwtToken) {
        this.message = message;
        this.state = state;
        this.jwtToken = jwtToken;
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

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}
