package com.app.responses;

public class RegisterResponse extends BasicResponse{

    private String otp;


    public RegisterResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }
}
