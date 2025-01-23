package com.app.controllers;

import com.app.entities.UserEntity;
import com.app.responses.RegisterResponse;
import com.app.service.Persist;
import com.app.utils.GeneralUtils;
import com.app.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;

import static com.app.utils.Constants.*;

@RestController
public class GeneralController {
    private HashMap<String,UserEntity> unverifiedUsers = new HashMap<>();

    @Autowired
    private Persist persist;

    @PostConstruct
    public void init(){
//        UserEntity admin = new UserEntity();
//        admin.setUsername("admin");
//        admin.setPassword("admin12345");
//        admin.setEmail("admin12345@gmail.com");
//        this.persist.save(admin);
    }

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
                String hashed = GeneralUtils.hashMd5(user.getUsername(), user.getPassword());
                user.setPassword(hashed);
                String otp = GeneralUtils.generateOtp();
                user.setOtp(otp);
            }
        }
        return null;
    }


}
