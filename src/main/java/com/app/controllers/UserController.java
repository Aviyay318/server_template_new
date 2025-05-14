package com.app.controllers;


import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.IslandsEntity;
import com.app.entities.QuestionTypeEntity;
import com.app.entities.UserEntity;
import com.app.service.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
    public Map<String, Object> getStatisticData(@RequestParam String token) {
        Map<String, Object> statisticData = new HashMap<>();

        UserEntity user = this.persist.getUserByToken(token);
        if (user == null) {
            statisticData.put("error", "משתמש לא נמצא");
            return statisticData;
        }

        List<ExerciseHistoryEntity> exercises = this.persist.getExercisesByUserId(user);
        int totalQuestions = exercises.size();

        long correctAnswers = exercises.stream().filter(ExerciseHistoryEntity::isCorrectAnswer).count();
        long incorrectAnswers = exercises.stream()
                .filter(e -> !e.isCorrectAnswer() && e.getAnswer() != null && !e.getAnswer().isEmpty()).count();
        long unanswered = exercises.stream()
                .filter(e -> e.getAnswer() == null || e.getAnswer().isEmpty()).count();

        double averageSolutionTime = exercises.stream()
                .filter(e -> e.getSolutionTime() > 0)
                .mapToDouble(ExerciseHistoryEntity::getSolutionTime)
                .average().orElse(0);

        double correctRate = totalQuestions == 0 ? 0 : (correctAnswers * 100.0) / totalQuestions;

        statisticData.put("totalQuestions", totalQuestions);
        statisticData.put("correctAnswers", correctAnswers);
        statisticData.put("incorrectAnswers", incorrectAnswers);
        statisticData.put("unanswered", unanswered);
        statisticData.put("correctRate", String.format("%.2f", correctRate));
        statisticData.put("averageSolutionTime", String.format("%.2f", averageSolutionTime));

        // 🔹 סטטיסטיקה לפי סוג שאלה
        Map<String, Map<String, Object>> questionTypeStats = new HashMap<>();
        exercises.stream()
                .filter(e -> e.getQuestionType() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getQuestionType().getName()
                ))
                .forEach((type, list) -> {
                    long correct = list.stream().filter(ExerciseHistoryEntity::isCorrectAnswer).count();
                    double avgTime = list.stream().mapToDouble(ExerciseHistoryEntity::getSolutionTime).average().orElse(0);
                    double rate = list.size() == 0 ? 0 : (correct * 100.0) / list.size();

                    Map<String, Object> typeData = new HashMap<>();
                    typeData.put("total", list.size());
                    typeData.put("correct", correct);
                    typeData.put("correctRate", String.format("%.2f", rate));
                    typeData.put("averageTime", String.format("%.2f", avgTime));

                    questionTypeStats.put(type, typeData);
                });

        statisticData.put("byQuestionType", questionTypeStats);

        // 🔹 לפי אי וסוג שאלה
        Map<String, Map<String, Long>> perIslandStats = new HashMap<>();
        for (ExerciseHistoryEntity e : exercises) {
            if (e.getIslands() == null || e.getQuestionType() == null) continue;

            String island = e.getIslands().getName();
            String type = e.getQuestionType().getName();

            perIslandStats
                    .computeIfAbsent(island, k -> new HashMap<>())
                    .merge(type, 1L, Long::sum);
        }

        statisticData.put("byIslandAndType", perIslandStats);
// 🔹 התקדמות לפי תאריך
        Map<String, List<ExerciseHistoryEntity>> byDate = exercises.stream()
                .filter(e -> e.getCreatedAt() != null)
                .collect(Collectors.groupingBy(e -> e.getCreatedAt().toString()));

        List<Map<String, Object>> progressByDate = new ArrayList<>();

        for (String date : byDate.keySet()) {
            List<ExerciseHistoryEntity> list = byDate.get(date);
            long correct = list.stream().filter(ExerciseHistoryEntity::isCorrectAnswer).count();
            int total = list.size();
            double rate = total == 0 ? 0 : (correct * 100.0) / total;

            Map<String, Object> daily = new HashMap<>();
            daily.put("date", date);
            daily.put("total", total);
            daily.put("correct", correct);
            daily.put("correctRate", String.format("%.2f", rate));

            progressByDate.add(daily);
        }

        statisticData.put("progressByDate", progressByDate);

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

    @RequestMapping("/get-user-stats")
    public Map<String, Object> getUserStats(String token, int islandId) {
        Map<String, Object> userStats = new HashMap<>();

        try {
            UserEntity user = this.persist.getUserByToken(token);
            if (user == null) {
                userStats.put("error", "User not found");
                return userStats;
            }

            IslandsEntity island = this.persist.loadObject(IslandsEntity.class, islandId);
            if (island == null) {
                userStats.put("error", "Island not found");
                return userStats;
            }

            List<ExerciseHistoryEntity> userHistory = this.persist.getExercisesByUserIdAndIsland(user, island);
            if (userHistory == null || userHistory.isEmpty()) {
                userStats.put("bestTime", 0);
                userStats.put("bestDay", "אין כרגע");
                userStats.put("bestStreak", 0);
                return userStats;
            }

            double bestTime = userHistory.stream()
                    .filter(e -> e.isCorrectAnswer() && e.getSolutionTime() > 0)
                    .mapToDouble(ExerciseHistoryEntity::getSolutionTime)
                    .min()
                    .orElse(0);

            Map<Date, Long> correctPerDay = userHistory.stream()
                    .filter(e -> e.isCorrectAnswer() && e.getCreatedAt() != null)
                    .collect(Collectors.groupingBy(
                            e -> e.getCreatedAt(),
                            Collectors.counting()
                    ));

            String bestDayStr = correctPerDay.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> entry.getKey().toString())
                    .orElse("אין כרגע");

            int bestStreak = 0;
            int currentStreak = 0;

            List<ExerciseHistoryEntity> sortedHistory = userHistory.stream()
                    .filter(e -> e.getCreatedAt() != null)
                    .sorted(Comparator.comparing(ExerciseHistoryEntity::getCreatedAt))
                    .toList();

            for (ExerciseHistoryEntity e : sortedHistory) {
                if (e.isCorrectAnswer()) {
                    currentStreak++;
                    bestStreak = Math.max(bestStreak, currentStreak);
                } else {
                    currentStreak = 0;
                }
            }

            userStats.put("bestTime", bestTime);
            userStats.put("bestDay", bestDayStr);
            userStats.put("bestStreak", bestStreak);

        } catch (Exception ex) {
            System.err.println("Exception in /get-user-stats: " + ex.getMessage());
            userStats.put("error", "Server error: " + ex.getMessage());
        }

        return userStats;
    }




}
