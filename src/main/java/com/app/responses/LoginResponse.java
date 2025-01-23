package com.app.responses;

public class LoginResponse extends BasicResponse {
    private String token;
    private boolean isAdmin;

    public LoginResponse(boolean success,String token,boolean isAdmin) {
        super(success);
        this.token = token;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
