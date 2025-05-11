package com.app.entities;

public class LevelsEntity extends BaseEntity{
    private UserEntity user;
    private IslandsEntity island;
    private int level;
private double progress;

    public LevelsEntity() {
    }

    public LevelsEntity(UserEntity user, IslandsEntity island, int level) {
        this.user = user;
        this.island = island;
        this.level = level;
        this.progress = 0;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public IslandsEntity getIsland() {
        return island;
    }

    public void setIsland(IslandsEntity island) {
        this.island = island;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "LevelsEntity{" +
                "user=" + user +
                ", island=" + island +
                ", level=" + level +
                '}';
    }
}
