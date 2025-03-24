package com.app.controllers;

import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.UserEntity;
import com.app.service.Persist;
import com.app.utils.LevelUp;
import com.app.utils.MathExercise;
import com.app.utils.QuestionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        mathExercise.setId(exerciseHistory.getId());
        // החזרת JSON
        return mathExercise.toJson();
    }

    @RequestMapping("/get-level")
    public int getLevel(String token){
        UserEntity user = this.persist.getUserByToken(token);
       return LevelUp.getLevelOfUser(this.persist.getExercisesByUserId(user));
    }
    @RequestMapping("/check-exercise")
    public boolean checkExercise(String token, int id,String answer){
        ExerciseHistoryEntity exerciseHistory = this.persist.loadObject(ExerciseHistoryEntity.class,id);
        if (exerciseHistory.getAnswer().equals(answer)) {
            exerciseHistory.setCorrectAnswer(true);
            this.persist.save(exerciseHistory);
            return true;
        }
        return false;
    }

    @RequestMapping("/get-literal-problem")
    public Map<String, Object> getLiteralProblem(@RequestParam String token) {
        UserEntity user = this.persist.getUserByToken(token);
        System.out.println(user);
        Map<String, Object> literalProblem = QuestionGenerator.generateQuestion();
       System.out.println(literalProblem.get("answer"));
     ExerciseHistoryEntity exerciseHistory = new ExerciseHistoryEntity(user,1,(String) literalProblem.get("question"),false, String.valueOf(literalProblem.get("answer")));
       System.out.println(exerciseHistory.getAnswer());
       this.persist.save(exerciseHistory);
       literalProblem.put("id",exerciseHistory.getId());
        return literalProblem;

    }

    @RequestMapping("/check-literal-problem")
    public boolean checkLiteralProblem(String token, int id){
        return false;
    }
}
