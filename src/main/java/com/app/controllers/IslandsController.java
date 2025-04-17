package com.app.controllers;

import com.app.Math.AddAndSubIslandService;
import com.app.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/islands")
public class IslandsController {

    @Autowired
    private AddAndSubIslandService addAndSubIslandService;

    @RequestMapping("/Addition-and-subtraction")
    public Map<String, Object> addAndSub(@RequestParam String token, @RequestParam int questionType) {
        return this.addAndSubIslandService.generateExercise(token, questionType, Constants.ADD_SUB_ISLAND);
    }

    @PostConstruct
    public void init() {
        System.out.println(addAndSub("81E06B9847197BCA58F9BBB424825215", 1));
    }
}
