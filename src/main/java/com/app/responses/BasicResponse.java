package com.app.responses;

public class BasicResponse {
    private boolean success;


    public BasicResponse(boolean success) {
        this.success = success;

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
