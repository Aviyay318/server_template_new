package com.app.responses;

public class OtpResponse {
    private boolean success;
    private String message;
    private boolean registeredSuccessfully;

    public OtpResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public OtpResponse(boolean success, String message, boolean registeredSuccessfully) {
        this.success = success;
        this.message = message;
        this.registeredSuccessfully = registeredSuccessfully;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRegisteredSuccessfully() {
        return registeredSuccessfully;
    }

    public void setRegisteredSuccessfully(boolean registeredSuccessfully) {
        this.registeredSuccessfully = registeredSuccessfully;
    }
}
