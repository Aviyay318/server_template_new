package com.app.controllers;

import com.app.entities.NotificationEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sse-notification")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationSseController {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/stream")
    public SseEmitter stream(@RequestParam String token) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        emitters.put(token, emitter);

        emitter.onCompletion(() -> emitters.remove(token));
        emitter.onTimeout(() -> emitters.remove(token));
        emitter.onError((e) -> emitters.remove(token));

        return emitter;
    }

    public void sendToAllClients(NotificationEntity notification) {
        List<SseEmitter> toRemove = new ArrayList<>();
        for (SseEmitter emitter : emitters.values()) {
            try {
                emitter.send(Collections.singletonList(notification));
            } catch (IOException e) {
                toRemove.add(emitter);
            }
        }

        // ניקוי emitters שלא פעילים יותר
        toRemove.forEach(emitterToRemove ->
                emitters.entrySet().removeIf(entry -> entry.getValue().equals(emitterToRemove))
        );

    }
}
