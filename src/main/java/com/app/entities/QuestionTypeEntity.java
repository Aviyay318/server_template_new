package com.app.entities;

public class QuestionTypeEntity extends BaseEntity {
    private String name;

    public QuestionTypeEntity(String name) {
        this.name = name;
    }

    public QuestionTypeEntity(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
