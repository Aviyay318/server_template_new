package com.app.controllers;

import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.UserEntity;
import com.app.entities.NotificationEntity;
import com.app.responses.BasicResponse;
import com.app.service.Persist;
import com.app.utils.ApiEmailProcessor;
import com.app.utils.IslandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173") // CORS
public class AdminController {

    public static final String ADMIN = "admin";

    @Autowired
    private Persist persist;

    @Autowired
    private NotificationSseController notificationSseController;

    public static class EmailRequest {
        public String token;
        public List<String> emails;
        public String massage;
    }

    @PostMapping("/send-email-for-users")
    public BasicResponse sendEmailForUsers(@RequestBody EmailRequest request) {
        System.out.println("emails: " + request.emails);
        UserEntity user = this.persist.getUserByToken(request.token);

        boolean success = false;
        String message = "רק מנהל יכול לשלוח הודעות";

        if (user != null && user.getUsername().equals(ADMIN)) {
            for (String email : request.emails) {
                ApiEmailProcessor.sendEmail(email, "הודעת מנהל", request.massage);
            }
            success = true;
            message = "ההודעות נשלחו בהצלחה";

            NotificationEntity notification = new NotificationEntity();
            notification.setTitle("הודעת מנהל");
            notification.setContent("התקבלה הודעה במייל");
            notification.setTimestamp(System.currentTimeMillis());

            notificationSseController.sendToAllClients(notification);
        }

        return new BasicResponse(success, message);
    }
    @RequestMapping("/get-all-users")
    public List<UserEntity> getAllUsers(@RequestParam String token) {
        System.out.println(token);
        UserEntity user = persist.getUserByToken(token);
        if (user != null && ADMIN.equals(user.getUsername())) {
            return persist.loadList(UserEntity.class);
        }
        return List.of();
    }

    @RequestMapping("/get-user-info")
    public Map<String,Object> getUserInfo(String token, String email) {
        System.out.println(email + " email");
        System.out.println(token + "token");
        Map<String,Object> userInfo = new HashMap<>();
        System.out.println(token);
        UserEntity admin = persist.getUserByToken(token);
        if (admin != null && ADMIN.equals(admin.getUsername())) {
           UserEntity user = this.persist.getUserByEmail(email);
           if (user!=null){
               List<ExerciseHistoryEntity> userHistory = this.persist.getExercisesByUserId(user);

               userInfo.put("username",user.getUsername());
               userInfo.put("correctAnswer",userHistory.stream().filter(ExerciseHistoryEntity::isCorrectAnswer).toList().size());
               userInfo.put("wrongAnswer",userHistory.stream().filter(exerciseHistoryEntity -> !exerciseHistoryEntity.isCorrectAnswer()).toList().size());
               userInfo.put("openIsland" , IslandUtils.getIslandsWithOpenStatus(this.persist,user.getPassword()));
               userInfo.put("score",user.getScore());
               userInfo.put("exercise",userHistory.size());
               userInfo.put("userHistory",userHistory);
           }
        }
        System.out.println(userInfo);
return userInfo;
    }

    @GetMapping("/statistics")
    public Map<String, Object> getSystemStatistics(@RequestParam String token) {
        List<UserEntity> users = this.persist.loadList(UserEntity.class);
        List<ExerciseHistoryEntity> history = this.persist.loadList(ExerciseHistoryEntity.class);

        int totalUsers = users.size();
        int totalExercises = history.size();
        long totalCorrect = history.stream().filter(ExerciseHistoryEntity::isCorrectAnswer).count();
        double avgScore = users.stream().mapToInt(UserEntity::getScore).average().orElse(0);

        double avgOpenIslands = users.stream()
                .mapToInt(user -> IslandUtils.getOpenIslandsForUser(persist, user).size())
                .average()
                .orElse(0);

        Optional<UserEntity> topUser = users.stream()
                .max(Comparator.comparingInt(u ->
                        (int) history.stream()
                                .filter(e -> e.getUserId().equals(u) && e.isCorrectAnswer())
                                .count()
                ));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalExercises", totalExercises);
        stats.put("avgCorrectRate", totalExercises == 0 ? 0 : (totalCorrect * 100.0) / totalExercises);
        stats.put("avgScore", avgScore);
        stats.put("avgOpenIslands", avgOpenIslands);
        stats.put("topUser", topUser.map(UserEntity::getUsername).orElse("אין"));

        return stats;
    }

}
