package com.app.controllers;

import com.app.MathIslands.AddAndSubIslandService;
import com.app.MathIslands.MultiplicationIslandService;
import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.IslandsEntity;
import com.app.entities.LevelsEntity;
import com.app.entities.UserEntity;
import com.app.responses.CheckExerciseResponse;
import com.app.service.Persist;
import com.app.utils.Constants;
import com.app.utils.IslandUtils;
import com.app.utils.LevelUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/islands")
public class IslandsController {

    @Autowired
    private Persist persist;

    @Autowired
    private AddAndSubIslandService addAndSubIslandService;

    @Autowired
    private MultiplicationIslandService multiplicationIslandService;

    @RequestMapping("/Addition-and-subtraction")
    public Map<String, Object> addAndSub(@RequestParam String token, @RequestParam int questionType) {
        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,Constants.ADD_SUB_ISLAND);
        LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user,island);
        return addAndSubIslandService.generateExercise(user,island, islandLevel, questionType);
    }
    @RequestMapping("/multiplication")
    public Map<String, Object> multiplicationTable(@RequestParam String token, @RequestParam int questionType) {

        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,Constants.MULTIPLICATION_ISLAND);
        System.out.println(island);
        LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user,island);
        System.out.println(islandLevel);
        return multiplicationIslandService.generateExercise(user,island, islandLevel, questionType);
    }

    @RequestMapping("/check-exercise")
    public CheckExerciseResponse checkExercise(String token , int exerciseId, String answer, int solution_time, boolean usedClue , int questionType){
        UserEntity user = this.persist.getUserByToken(token);
        String islandOpen = null;
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
               }else {
                   score = 2;
               }
               success = true;
               message = "great gob";
           }else {
               score-=2;
               message = "wrong answer";
           }
           if (user!=null){
               score +=user.getScore();
               user.setScore(score);
               this.persist.save(user);
           }
           IslandsEntity island = this.persist.loadObject(IslandsEntity.class,exerciseHistory.getIslands().getId());
           System.out.println(island.getName());
           LevelsEntity islandLevel = this.persist.getLevelByUserIdAndIslandId(user,island);
//           List<ExerciseHistoryEntity> exerciseHistoryList = this.persist.getExercisesByUserIdAndLevel(user,islandLevel.getLevel());
           List<ExerciseHistoryEntity> exerciseHistoryList = this.persist.getExercisesByUserIdAndIsland(user,islandLevel);
           int level = LevelUp.getLevelOfUser(exerciseHistoryList);
           System.out.println("level: " + level);
           islandLevel.setLevel(level);
           this.persist.save(islandLevel);
           islandOpen =  openIslands(score,user);
       }catch (Exception e){}
        return new CheckExerciseResponse(success,message,user,islandOpen);
    }

    private String openIslands(int score, UserEntity user) {
        String islandOpen = null;
        List<IslandsEntity> allIslands = this.persist.loadList(IslandsEntity.class);
        List<LevelsEntity> userLevels = this.persist.getLevelsByUserId(user);

        for (IslandsEntity island : allIslands) {
            if (score >= island.getScore()) {
                boolean alreadyUnlocked = userLevels.stream()
                        .anyMatch(level -> level.getIsland().getId()==(island.getId()));
                if (!alreadyUnlocked) {
                    LevelsEntity newLevel = new LevelsEntity(user, island, 1);
                    this.persist.save(newLevel);
                    islandOpen = "Island " + island.getName() + " unlocked for you";
                    System.out.println("Island " + island.getName() + " unlocked for user " + user.getUsername());
                }
            }
        }
        return islandOpen;
    }
    @RequestMapping("/get-user-open-island")
    public List<Map<String, Object>> getUserOpenIsland(@RequestParam String token) {
        return IslandUtils.getIslandsWithOpenStatus(persist, token);
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

    @RequestMapping("/get-level-by-island")
    public int getLevelByIsland(String token,int islandId){
        UserEntity user = this.persist.getUserByToken(token);
        IslandsEntity island = this.persist.loadObject(IslandsEntity.class,islandId);
        LevelsEntity level =  this.persist.getLevelByUserIdAndIslandId(user,island);
        return level.getLevel();
    }
    @RequestMapping("/get-level-by-user")
    public List<LevelsEntity>  getLevelByUser(String token){
        UserEntity user = this.persist.getUserByToken(token);
        List<LevelsEntity> levels =  this.persist.getLevelsByUserId(user);
        return levels;
    }

     @RequestMapping("/islands")
     public List<IslandsEntity> getIslands(){
        return this.persist.loadList(IslandsEntity.class);
     }
    @PostConstruct
    public void init() {
//       System.out.println(multiplicationTable("C34B1B2B6FCFC8307CE1A78006DD6A0E",4));
    //   System.out.println(checkExercise(14,"5",10));
    }
}
