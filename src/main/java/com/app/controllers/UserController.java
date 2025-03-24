package com.app.controllers;


import com.app.entities.UserEntity;
import com.app.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private Persist persist;


    @RequestMapping("/get-user-data")
    public UserEntity getUserData(String token){
        System.out.println("!!!!");
        UserEntity userEntity= null;
        if (!token.isEmpty()){
            userEntity  =  this.persist.getUserByToken(token);
        }
        System.out.println("token: "+token);

        return userEntity;
    }

    @RequestMapping("/get-username")
    public String getUsername(String token){
        UserEntity user = this.persist.getUserByToken(token);
        if (user!=null){
            return user.getUsername();
        }
        System.out.println("USER: "+user);
        return null;
    }


}
