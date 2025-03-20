package com.app.utils;

import com.app.entities.ExerciseHistoryEntity;

import java.util.List;

public class LevelUp {

    public static int getLevelOfUser(List<ExerciseHistoryEntity> exerciseHistoryOfUser) {
        int totalPoints = 0;

        if (exerciseHistoryOfUser != null && !exerciseHistoryOfUser.isEmpty()) {
            for (ExerciseHistoryEntity exercise : exerciseHistoryOfUser) {
                if (exercise.isCorrectAnswer()) { // רק אם התשובה נכונה
                    totalPoints += exercise.getLevel(); // ניקוד לפי רמת התרגיל

                    if (exercise.getSolutionTime() < 1.0) { // אם פתר בפחות מדקה
                        totalPoints += 2; // בונוס 2 נקודות
                    }
                }
            }
        }

        return (totalPoints / 10) + 1; // כל 10 נקודות = רמה חדשה, מתחיל מרמה 1
    }



}
