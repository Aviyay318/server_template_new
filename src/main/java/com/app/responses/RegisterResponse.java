package com.app.responses;

public class RegisterResponse extends BasicResponse{
    private Integer errorCode;
    private String otp;


    public RegisterResponse(boolean success, Integer errorCode,String otp) {
        super(success);
        this.errorCode = errorCode;
        this.otp = otp;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
