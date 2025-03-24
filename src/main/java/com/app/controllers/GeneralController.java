package com.app.controllers;

import com.app.responses.LoginResponse;
import com.app.entities.UserEntity;
import com.app.responses.OtpResponse;
import com.app.responses.RegisterResponse;
import com.app.service.Persist;
import com.app.utils.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@RestController
public class GeneralController {
    private HashMap<String,UserEntity> unverifiedUsers = new HashMap<>();

    @Autowired
    private Persist persist;


    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object hello() {
        return "Hello From Server";
    }

    @PostConstruct
    public void init(){
     //   ApiEmailProcessor.sendEmail("byhyhzql@gmail.com","try","try to");
    }

//    @PostMapping("/register")
//    public RegisterResponse getUser(@RequestBody UserEntity user) { // ✔️ מקבל ישירות UserEntity
//        System.out.println("Received User: " + user);
//
//        boolean success = false;
//        Integer errorCode = Constants.EMAIL_EXIST;
//        String otp = null;
//
//        if (user != null) {
//            if (this.persist.getUserByEmail(user.getEmail()) == null) {
//                if (this.unverifiedUsers.get(user.getEmail()) != null) {
//                    String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
//                    user.setPassword(hashed);
//                    otp = GeneralUtils.generateOtp();
//                    user.setOtp(otp);
//                } else {
//                    this.unverifiedUsers.put(user.getEmail(), user);
//                }
//                System.out.println(ApiEmailProcessor.sendEmail(user.getEmail(), "opt:", otp));
//                String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
//                user.setPassword(hashed);
//                otp = GeneralUtils.generateOtp();
//                System.out.println("This is OTP: " + otp);
//                user.setOtp(otp);
//                if (otp.equals(user.getOtp())) {
//
//                }
//                success = true;
//                errorCode = null;
//                this.persist.save(user);
//                System.out.println("Saved User: " + user);
//            }
//        }
//
//        return new RegisterResponse(success, errorCode, otp);
//    }

    @PostMapping("/register")
    public RegisterResponse registerUser (@RequestBody UserEntity user) {
        System.out.println("Received User " + user);

        boolean success = false;
        Integer errorCode = Constants.EMAIL_EXIST;
        String otp = null;

        if (user != null && persist.getUserByEmail(user.getEmail()) == null) {
            otp = GeneralUtils.generateOtp();
            user.setOtp(otp);

            String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
            user.setPassword(hashed);

            unverifiedUsers.put(user.getEmail(),user);
            boolean emailSent = ApiEmailProcessor.sendEmail(user.getEmail(), "OTP Verification", "Here is your code: " + otp);
            System.out.println("OTP sent: " + emailSent);
            success = true;
            errorCode=null;
        }
        return new RegisterResponse(success,errorCode,otp);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        boolean success = false;
        String token = null;
        boolean isAdmin = false;
        String hashed = GeneralUtils.hashMd5(email, password);
        UserEntity user = this.persist.getUserByEmailAndPassword(email);

        if (user!=null){
              if (user.getPassword().equals(hashed)||Constants.ADMIN_EMAIL.equals(email)&&Constants.ADMIN_PASSWORD.equals(password)) {
                success = true;
                token = hashed;
             if (Constants.ADMIN_EMAIL.equals(email)){
                 isAdmin = true;
             }
            }

        }
      return new LoginResponse(success,token,isAdmin);
    }

    @PostMapping ("/check-otp-to-register")
    public OtpResponse getOtp(@RequestBody OtpRequest otpRequest) {
        System.out.println(otpRequest);
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        UserEntity user = unverifiedUsers.get(email);
        if (user == null){
            return new OtpResponse(false, "User not found", false);
        }
        if (user.getOtp() != null && user.getOtp().equals(otp)){
            user.setOtp(null);
            persist.update(user);
            return new OtpResponse(true, "OTP verified successfully", true);
        } else{
            return new OtpResponse(false, "Invalid OTP", false);
        }
    }
}
