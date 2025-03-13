package com.app.controllers;

import com.app.utils.MathExercise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {

    // מטפל בבקשות GET ל-"/get-ex1"
    @GetMapping("/get-exercise")
    public MathExercise getExercise(String token ,int level) {
     return new MathExercise(level);


    }

}
