package com.app.entities;

public class LevelsEntity extends BaseEntity{
    private UserEntity user;
    private IslandsEntity island;
    private int level;
    private int highestLevel;

    public int getHighestLevel() {
        return highestLevel;
    }

    public void setHighestLevel(int highestLevel) {
        this.highestLevel = highestLevel;
    }
    public LevelsEntity() {
    }

    public LevelsEntity(UserEntity user, IslandsEntity island, int level) {
        this.user = user;
        this.island = island;
        this.level = level;
        this.highestLevel = highestLevel;
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
