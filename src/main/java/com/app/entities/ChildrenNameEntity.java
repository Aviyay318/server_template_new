package com.app.entities;

public class ChildrenNameEntity extends BaseEntity{
    private String name;
    private String gender;

    public ChildrenNameEntity(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }
    public ChildrenNameEntity(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "childrenNamesEntity{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
