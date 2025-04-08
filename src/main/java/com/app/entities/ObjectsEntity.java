package com.app.entities;

import lombok.Getter;
import lombok.Setter;

public class ObjectsEntity extends BaseEntity{
    @Getter
    @Setter
    private String pluralName;
    @Getter
    @Setter
    private String singularName;
    @Getter
    @Setter
    private String svg;

    public ObjectsEntity(String pluralName, String singularName, String svg) {
        this.pluralName = pluralName;
        this.singularName = singularName;
        this.svg = svg;
    }

    public ObjectsEntity(){}
    @Override
    public String toString() {
        return "ObjectsEntity{" +
                "pluralName='" + pluralName + '\'' +
                ", singularName='" + singularName + '\'' +
                ", svg='" + svg + '\'' +
                '}';
    }
}
