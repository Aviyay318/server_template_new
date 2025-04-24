package com.app.controllers;

import com.app.entities.UserEntity;
import com.app.entities.NotificationEntity;
import com.app.responses.BasicResponse;
import com.app.service.Persist;
import com.app.utils.ApiEmailProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            // ✉ שליחת Notification
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
}
