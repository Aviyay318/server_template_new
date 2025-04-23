package com.app.entities;

public class IslandsEntity extends BaseEntity{
    private String name;
    private int Score;

    public IslandsEntity() {
    }

    public IslandsEntity(String name, int score) {
        this.name = name;
        Score = score;
    }

    public IslandsEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    @Override
    public String toString() {
        return "IslandsEntity{" +
                "name='" + name + '\'' +
                ", Score=" + Score +
                '}';
    }
}
