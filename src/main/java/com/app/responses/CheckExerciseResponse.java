package com.app.responses;

import com.app.entities.LevelsEntity;
import com.app.entities.UserEntity;

public class CheckExerciseResponse extends BasicResponse{
    private UserEntity user;
    private LevelsEntity level;
    private String islandOpen;
    private double progress ;
    private String levelUp;
    public CheckExerciseResponse(boolean success) {
        super(success);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LevelsEntity getLevel() {
        return level;
    }

    public void setLevel(LevelsEntity level) {
        this.level = level;
    }

    public String getIslandOpen() {
        return islandOpen;
    }

    public void setIslandOpen(String islandOpen) {
        this.islandOpen = islandOpen;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(String levelUp) {
        this.levelUp = levelUp;
    }

    public CheckExerciseResponse(boolean success, String message, UserEntity user, String islandOpen, LevelsEntity level, double progress,String levelUp) {
        super(success, message);
        this.user = user;
        this.islandOpen = islandOpen;
        this.level = level;
        this.progress = progress;
        this.levelUp = levelUp;

    }


}
