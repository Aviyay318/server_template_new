package com.app.entities;

public class ExerciseDataEntity extends BaseEntity {
    private UserEntity userId;
    private int level;
    private int correctAnswer;
    private int correctQuestion;
    private int streak;


    public ExerciseDataEntity(UserEntity userId, int level, int correctAnswer, int correctQuestion, int streak) {
        this.userId = userId;
        this.level = level;
        this.correctAnswer = correctAnswer;
        this.correctQuestion = correctQuestion;
        this.streak = streak;
    }

    public ExerciseDataEntity() {
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getCorrectQuestion() {
        return correctQuestion;
    }

    public void setCorrectQuestion(int correctQuestion) {
        this.correctQuestion = correctQuestion;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }
}
