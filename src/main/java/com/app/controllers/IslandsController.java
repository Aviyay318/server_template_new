package com.app.controllers;

import com.app.Math.AddAndSubIslandService;
import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.IslandsEntity;
import com.app.entities.UserEntity;
import com.app.responses.BasicResponse;
import com.app.service.Persist;
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
    private Persist persist;

    @Autowired
    private AddAndSubIslandService addAndSubIslandService;

    @RequestMapping("/Addition-and-subtraction")
    public Map<String, Object> addAndSub(@RequestParam String token, @RequestParam int questionType) {
        return this.addAndSubIslandService.generateExercise(token, questionType, Constants.ADD_SUB_ISLAND);
    }
    //TODO
//    @RequestMapping("/multiplication table")
//    public Map<String, Object> multiplicationTable(@RequestParam String token, @RequestParam int questionType){
//
//    }

    @RequestMapping("/check-exercise")
    public BasicResponse checkExercise(String token , int exerciseId, String answer, int solution_time,boolean usedClue ,int questionType){
        UserEntity user = this.persist.getUserByToken(token);
        System.out.println("s: " + solution_time);
       boolean success = false;
       String message = "wrong question id";
       int score = 1;
       try{
           ExerciseHistoryEntity exerciseHistory = this.persist.getExerciseByExerciseId(exerciseId);
           if (exerciseHistory.getAnswer().equals(answer)) {
               exerciseHistory.setCorrectAnswer(true);
               exerciseHistory.setSolutionTime(solution_time);
               this.persist.save(exerciseHistory);
               if (!usedClue){
                   if (questionType == Constants.LITERAL_PROBLEMS){
                       score = 5;
                   } else if (questionType ==Constants.COMPLETE_TABLE&&solution_time<2) {
                       score = 7;
                   }else {
                       score++;
                   }
               }
               if (user!=null){
                   user.setScore(score);
                   this.persist.save(user);
               }
               success = true;
               message = "great gob";
           }else {
               message = "wrong answer";
           }
       }catch (Exception e){}
       //הניקוד שלו במספרים ,
        return new BasicResponse(success,message);
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

     @RequestMapping("/islands")
     public List<IslandsEntity> getIslands(){
        return this.persist.loadList(IslandsEntity.class);
     }
    @PostConstruct
    public void init() {
    //   System.out.println(checkExercise(14,"5",10));
    }
}
