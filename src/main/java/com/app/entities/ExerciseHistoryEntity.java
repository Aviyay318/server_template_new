package com.app.entities;

public class ExerciseHistoryEntity extends BaseEntity{
    private UserEntity userId;
    private String exercise;
    private int level;
    private boolean isCorrectAnswer;
    private String answer;
   private double solutionTime;

    public ExerciseHistoryEntity(UserEntity userId, int level, String exercise, boolean isCorrectAnswer,String answer) {
        this.userId = userId;
        this.level = level;
        this.isCorrectAnswer = isCorrectAnswer;
        this.exercise = exercise;
        this.answer = answer;
        this.solutionTime=0;
    }


    public ExerciseHistoryEntity() {
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public boolean getIsCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setIsCorrectAnswer(boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public double getSolutionTime() {
        return solutionTime;
    }

    public void setSolutionTime(double solutionTime) {
        this.solutionTime = solutionTime;
    }
}
