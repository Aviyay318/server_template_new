package com.app.utils;

import com.app.entities.ExerciseHistoryEntity;

import java.util.List;

public class LevelUp {

    // קבועים לקונפיגורציה נוחה
    public static final int MIN_LEVEL = 1;

    public static final int FAST_COMPLETION_TIME = 30; // שניות

    // דרישות לקידום מואץ
    public static final double EXCELLENT_SUCCESS_RATE = 0.9;
    public static final int EXCELLENT_STREAK = 5;
    public static final int EXCELLENT_FAST_ANSWERS = 3;
    public static final int FAST_PROMOTION_DIVIDER = 5;

    // דרישות לקידום רגיל
    public static final double AVERAGE_SUCCESS_RATE = 0.5;
    public static final int REGULAR_PROMOTION_DIVIDER = 5;

    // דרישות לירידה ברמה
    public static final double POOR_SUCCESS_RATE = 0.3;
    public static final int MAX_WRONG_STREAK_FOR_DEMOTION = 5;

    public static int getLevelOfUser(List<ExerciseHistoryEntity> history) {
        if (history == null || history.isEmpty()) return MIN_LEVEL;

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

        // לוגיקה חכמה לקידום
        if (successRate >= EXCELLENT_SUCCESS_RATE &&
                bestStreak >= EXCELLENT_STREAK &&
                fastCorrect >= EXCELLENT_FAST_ANSWERS) {

            return totalCorrect / FAST_PROMOTION_DIVIDER + MIN_LEVEL;

        } else if (successRate >= AVERAGE_SUCCESS_RATE) {

            return totalCorrect / REGULAR_PROMOTION_DIVIDER + MIN_LEVEL;

        } else if (successRate < POOR_SUCCESS_RATE &&
                maxWrongStreak >= MAX_WRONG_STREAK_FOR_DEMOTION) {

            return Math.max(MIN_LEVEL, (totalCorrect / REGULAR_PROMOTION_DIVIDER));

        } else {
            return totalCorrect / REGULAR_PROMOTION_DIVIDER + MIN_LEVEL;
        }
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

        return (double) totalCorrect / total; // בין 0.0 ל-1.0
    }
    public static double getDynamicSuccessRate(List<ExerciseHistoryEntity> history) {
        if (history == null || history.isEmpty()) return 0.0;

        int totalCorrect = 0;
        int total = history.size();
        int streak = 0;
        int streakBonusCount = 0;
        int fastCorrect = 0;

        for (ExerciseHistoryEntity e : history) {
            if (e.isCorrectAnswer()) {
                totalCorrect++;
                streak++;
                if (e.getSolutionTime() < FAST_COMPLETION_TIME) {
                    fastCorrect++;
                }
            } else {
                if (streak >= 2) { // נחשב סטרייק אם יש לפחות 2 רצופים
                    streakBonusCount++;
                }
                streak = 0;
            }
        }

        // אם הסיום היה בסטרייק, קח גם אותו
        if (streak >= 2) {
            streakBonusCount++;
        }

        double baseRate = (double) totalCorrect / total;
        double bonus = streakBonusCount * 0.05 + fastCorrect * 0.05;
        double totalScore = baseRate + bonus;

        return Math.min(1.0, totalScore); // לא לעבור את 100%
    }

}
