package com.app.entities;

public class ExerciseHistoryEntity extends BaseEntity{
    private UserEntity userId;
    private String exercise;
    private int level;
    private boolean isCorrectAnswer;
    private String answer;
    private QuestionTypeEntity questionType;
   private double solutionTime;
private IslandsEntity islands;
private int exerciseId;

    public ExerciseHistoryEntity(UserEntity userId, int level, String exercise, boolean isCorrectAnswer,String answer,QuestionTypeEntity questionType) {
        this.userId = userId;
        this.level = level;
        this.isCorrectAnswer = isCorrectAnswer;
        this.exercise = exercise;
        this.answer = answer;
        this.questionType = questionType;
        this.solutionTime=0;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public IslandsEntity getIslands() {
        return islands;
    }

    public void setIslands(IslandsEntity islands) {
        this.islands = islands;
    }

    public QuestionTypeEntity getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionTypeEntity questionType) {
        this.questionType = questionType;
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

    @Override
    public String toString() {
        return "ExerciseHistoryEntity{" +
                "userId=" + userId +
                ", exercise='" + exercise + '\'' +
                ", level=" + level +
                ", isCorrectAnswer=" + isCorrectAnswer +
                ", answer='" + answer + '\'' +
                ", questionType=" + questionType +
                ", solutionTime=" + solutionTime +
                ", islands=" + islands +
                ", exerciseId=" + exerciseId +
                '}';
    }
}
