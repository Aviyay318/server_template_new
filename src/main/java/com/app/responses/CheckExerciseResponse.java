package com.app.responses;

import com.app.entities.UserEntity;

public class CheckExerciseResponse extends BasicResponse{
    private UserEntity user;
    private int level;
    private String islandOpen;

    public CheckExerciseResponse(boolean success) {
        super(success);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIslandOpen() {
        return islandOpen;
    }

    public void setIslandOpen(String islandOpen) {
        this.islandOpen = islandOpen;
    }

    public CheckExerciseResponse(boolean success, String message, UserEntity user,String islandOpen,int level) {
        super(success, message);
        this.user = user;
        this.islandOpen = islandOpen;
        this.level = level;
    }
}
