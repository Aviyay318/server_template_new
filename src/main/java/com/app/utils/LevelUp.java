package com.app.utils;

import com.app.entities.ExerciseHistoryEntity;
import java.util.*;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class LevelUp {

    public static final int MIN_LEVEL = 1;
    public static final int FAST_COMPLETION_TIME = 30;

    public static final double EXCELLENT_SUCCESS_RATE = 0.9;
    public static final int EXCELLENT_STREAK = 5;
    public static final int EXCELLENT_FAST_ANSWERS = 3;
    public static final int FAST_PROMOTION_DIVIDER = 5;

    public static final double AVERAGE_SUCCESS_RATE = 0.5;
    public static final int REGULAR_PROMOTION_DIVIDER = 5;

    public static final double POOR_SUCCESS_RATE = 0.3;
    public static final int MAX_WRONG_STREAK_FOR_DEMOTION = 5;

    public static final int IMMUNITY_AFTER_PROMOTION = 10;

    public static Map<String, Object> calculateLevelProgress(List<ExerciseHistoryEntity> fullHistory, int currentLevel) {
        Map<String, Object> result = new HashMap<>();

        List<ExerciseHistoryEntity> history = fullHistory.stream()
                .filter(e -> e.getLevel() == currentLevel)
                .collect(Collectors.toList());

        if (history == null || history.isEmpty()) {
            result.put("calculatedLevel", currentLevel);
            result.put("successRate", 0.0);
            result.put("progressToNextLevel", 0.0);
            result.put("statusMessage", null);
            return result;
        }

        int totalCorrect = 0;
        int total = history.size();
        int streak = 0;
        int bestStreak = 0;
        int fastCorrect = 0;
        int wrongStreak = 0;
        int maxWrongStreak = 0;

        for (ExerciseHistoryEntity e : history) {
            if (e.isCorrectAnswer()) {
                totalCorrect++;
                streak++;
                wrongStreak = 0;
                if (e.getSolutionTime() < FAST_COMPLETION_TIME) {
                    fastCorrect++;
                }
            } else {
                bestStreak = Math.max(bestStreak, streak);
                streak = 0;
                wrongStreak++;
                maxWrongStreak = Math.max(maxWrongStreak, wrongStreak);
            }
        }

        bestStreak = Math.max(bestStreak, streak);
        double successRate = (double) totalCorrect / total;
        result.put("successRate", successRate);

        int nextLevelDivider = successRate >= EXCELLENT_SUCCESS_RATE ? FAST_PROMOTION_DIVIDER : REGULAR_PROMOTION_DIVIDER;
        int neededForNextLevel = (currentLevel + 1 - MIN_LEVEL) * nextLevelDivider;
        double progress = Math.min(1.0, (double) totalCorrect / neededForNextLevel);
        result.put("progressToNextLevel", progress * 100); // אחוזים

        int newLevel = currentLevel;

        if (history.size() >= IMMUNITY_AFTER_PROMOTION &&
                successRate >= EXCELLENT_SUCCESS_RATE &&
                bestStreak >= EXCELLENT_STREAK &&
                fastCorrect >= EXCELLENT_FAST_ANSWERS) {
            newLevel = Math.max(currentLevel, totalCorrect / FAST_PROMOTION_DIVIDER + MIN_LEVEL);
        } else if (successRate >= AVERAGE_SUCCESS_RATE) {
            newLevel = Math.max(currentLevel, totalCorrect / REGULAR_PROMOTION_DIVIDER + MIN_LEVEL);
        }

        result.put("calculatedLevel", newLevel);
        result.put("statusMessage", newLevel > currentLevel ? "כל הכבוד! עברת לשלב הבא 🎉" : null);

        return result;
    }


    public static double getSuccessRate(List<ExerciseHistoryEntity> history) {
        if (history == null || history.isEmpty()) return 0.0;

        int totalCorrect = 0;
        int total = history.size();

        for (ExerciseHistoryEntity e : history) {
            if (e.isCorrectAnswer()) {
                totalCorrect++;
            }
        }

        return (double) totalCorrect / total;
    }
}
