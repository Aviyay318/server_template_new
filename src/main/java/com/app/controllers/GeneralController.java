package com.app.controllers;

import com.app.responses.LoginResponse;
import com.app.entities.UserEntity;
import com.app.responses.RegisterResponse;
import com.app.service.Persist;
import com.app.utils.GeneralUtils;
import com.app.utils.Constants;
import com.app.utils.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RegisterResponse getUser(@RequestBody UserEntity user) {
        boolean success = false;
        Integer errorCode = Constants.EMAIL_EXIST;
        if (user!=null){
            if (this.unverifiedUsers.get(user.getUsername())!=null){
                String hashed = GeneralUtils.hashMd5(user.getEmail(), user.getPassword());
                user.setPassword(hashed);
                String otp = GeneralUtils.generateOtp();
                user.setOtp(otp);
                success = true;
                errorCode = null;
            }
        }
        return new RegisterResponse(success,errorCode);
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
