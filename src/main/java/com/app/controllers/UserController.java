package com.app.controllers;


import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.QuestionTypeEntity;
import com.app.entities.UserEntity;
import com.app.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private Persist persist;

    @RequestMapping("/get-user-score")
    public int getUserScore(@RequestParam String token){
        UserEntity userEntity= null;
        if (!token.isEmpty()){
            userEntity  =  this.persist.getUserByToken(token);
        }
        System.out.println("token: "+token);

        assert userEntity != null;
        return userEntity.getScore();
    }


    @RequestMapping("/get-user-data")
    public UserEntity getUserData(@RequestParam String token){
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
    @RequestMapping("/get-statistic-data")
    public Map<String, Object> getStatisticData(String token){
        Map<String, Object> statisticData = new HashMap<>();
        UserEntity user = this.persist.getUserByToken(token);
        List<ExerciseHistoryEntity> exercise = this.persist.getExercisesByUserId(user);
        int correctAnswer = exercise.stream().filter(ExerciseHistoryEntity::isCorrectAnswer).toList().size();
        statisticData.put("correctAnswer",correctAnswer);
        statisticData.put("inCorrectAnswer",exercise.size()-correctAnswer);
        statisticData.put("howMuchQuestion",exercise.size());

        Map<String, List<ExerciseHistoryEntity>> groupedByQuestionTypeName =
                exercise.stream()
                        .collect(Collectors.groupingBy(e -> e.getQuestionType().getName()));

        for (Map.Entry<String, List<ExerciseHistoryEntity>> entry : groupedByQuestionTypeName.entrySet()) {
            String hebrewName = entry.getKey();
            System.out.println(hebrewName);
            String englishName = getTypeName(hebrewName);
           statisticData.put("Arithmetic Operations",0);
           statisticData.put("literal Problems",0);
           statisticData.put("Equations",0);
           statisticData.put("Multiplication Table",0);
            List<ExerciseHistoryEntity> exercises = entry.getValue();
           statisticData.put(englishName,exercises.size());
            System.out.println("Type: " + englishName + ", Count: " + exercises.size());
        }
        return statisticData;
    }
    public String getTypeName(String type){
      return switch (type){
            case "פעולות חשבון"-> "Arithmetic Operations";
            case "בעיות מילוליות" -> "literal Problems";
            case "משוואות" -> "Equations";
            case "לוח הכפל" -> "Multiplication Table";
          default -> "null";
        };
    }


    @RequestMapping("/feedback")
    public Map<String, Object> getFeedbackConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put("optimal", Map.of("exp", 33.3333333, "stars", 3, "message", "מושלם", "maxTime", 10, "sound", "RIGHT_ANSWER"));
        config.put("regular", Map.of("exp", 20, "stars", 2, "message", "טוב מאוד", "maxTime", 15, "sound", "RIGHT_ANSWER"));
        config.put("delayed", Map.of("exp", 10, "stars", 1, "message", "מספיק", "maxTime", 30, "sound", "RIGHT_ANSWER"));
        config.put("usedAClue", Map.of("exp", 10, "stars", 1, "message", "טעון שיפור", "sound", "RIGHT_ANSWER"));
        config.put("wrong", Map.of("exp", 0, "stars", 0, "message", "נסה שוב", "sound", "WRONG_ANSWER"));

        return config;
    }

    @RequestMapping("/user-exercise-history")
    public List<ExerciseHistoryEntity> userExerciseHistory(String token){
        UserEntity user = this.persist.getUserByToken(token);
        return this.persist.getExercisesByUserId(user);
    }


}
