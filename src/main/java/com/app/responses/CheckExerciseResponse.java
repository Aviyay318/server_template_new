package com.app.responses;

import com.app.entities.UserEntity;

public class CheckExerciseResponse extends BasicResponse{
    private UserEntity user;
    private int level;
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

    public CheckExerciseResponse(boolean success, String message, UserEntity user) {
        super(success, message);
        this.user = user;
    }
}
