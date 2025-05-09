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
        int fastCorrect = 0;
        int bestStreak = 0;
        int streak = 0;

        for (ExerciseHistoryEntity e : history) {
            if (e.isCorrectAnswer()) {
                totalCorrect++;
                streak++;
                if (e.getSolutionTime() < FAST_COMPLETION_TIME) {
                    fastCorrect++;
                }
            } else {
                bestStreak = Math.max(bestStreak, streak);
                streak = 0;
            }
        }

        bestStreak = Math.max(bestStreak, streak);
        int totalWrong = total - totalCorrect;

        double successRate = (double) totalCorrect / total;
        result.put("successRate", successRate);

        // חישוב איכות התשובות
        double qualityBoost = successRate;
        if (bestStreak >= EXCELLENT_STREAK) qualityBoost += 0.1;
        if (fastCorrect >= EXCELLENT_FAST_ANSWERS) qualityBoost += 0.1;
        qualityBoost = Math.min(qualityBoost, 1.0);

        // בסיס קבוע לעלייה/ירידה (נגיד, מתוך 100)
        double baseUnit = 100.0 / (currentLevel * 5.0);

        // חישוב ההשפעה של הצלחות וכישלונות
        double gain = totalCorrect * baseUnit * qualityBoost;
        double loss = totalWrong * baseUnit * (1 - successRate);

        // פרוגרס נטו, תמיד בין 0 ל־1
        double rawProgress = gain - loss;
        double progress = Math.max(0, Math.min(100, rawProgress));
        if (history.get(history.size()-1).isCorrectAnswer()&&progress==0){
            progress = 1;
        }
        result.put("progressToNextLevel", progress);

        int newLevel = currentLevel;
        String statusMessage = null;

        if (progress >= 100.0) {
            newLevel++;
            statusMessage = "כל הכבוד! עברת לשלב הבא 🎉";
        }

        result.put("calculatedLevel", newLevel);
        result.put("statusMessage", statusMessage);
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
