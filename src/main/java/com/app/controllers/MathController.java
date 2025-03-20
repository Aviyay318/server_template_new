package com.app.controllers;

import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.UserEntity;
import com.app.service.Persist;
import com.app.utils.LevelUp;
import com.app.utils.MathExercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MathController {

    @Autowired
    private Persist persist;

    @GetMapping("/get-exercise")
    public Map<String, Object> getExercise(String token, int level) {

        // שליפת משתמש לפי טוקן
        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) {
            throw new RuntimeException("Invalid token");
        }

        // יצירת תרגיל
        MathExercise mathExercise = new MathExercise(level);

        // שמירת ההיסטוריה
        ExerciseHistoryEntity exerciseHistory = new ExerciseHistoryEntity(user, level, mathExercise.toString(), false,mathExercise.getSolution().toString());
        this.persist.save(exerciseHistory);

        // החזרת JSON
        return mathExercise.toJson();
    }

    @RequestMapping("/get-level")
    public int getLevel(String token){
        System.out.println(token);
        UserEntity user = this.persist.getUserByToken(token);
        System.out.println(user);
       return LevelUp.getLevelOfUser(this.persist.getExercisesByUserId(user.getId()));
    }

}
