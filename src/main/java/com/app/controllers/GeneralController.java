package com.app.controllers;

import com.app.responses.LoginResponse;
import com.app.entities.UserEntity;
import com.app.responses.RegisterResponse;
import com.app.service.Persist;
import com.app.utils.ApiEmailProcessor;
import com.app.utils.GeneralUtils;
import com.app.utils.Constants;
import com.app.utils.LoginRequest;
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

    @PostMapping("/register")
    public RegisterResponse getUser(@RequestBody UserEntity user) { // ✔️ מקבל ישירות UserEntity
        System.out.println("Received User: " + user);

        boolean success = false;
        Integer errorCode = Constants.EMAIL_EXIST;
        String otp = null;

        if (user != null) {
            if (this.persist.getUserByEmail(user.getEmail()) == null) {
                if (this.unverifiedUsers.get(user.getEmail()) != null) {
                    String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
                    user.setPassword(hashed);
                    otp = GeneralUtils.generateOtp();
                    user.setOtp(otp);
                } else {
                    this.unverifiedUsers.put(user.getEmail(), user);
                }
                // ApiEmailProcessor.sendEmail(user.getEmail(), "opt:", otp);
                String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
                user.setPassword(hashed);
                otp = GeneralUtils.generateOtp();
                user.setOtp(otp);
                success = true;
                errorCode = null;
                this.persist.save(user);
                System.out.println("Saved User: " + user);
            }
        }

        return new RegisterResponse(success, errorCode, otp);
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

}
