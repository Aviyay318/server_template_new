package com.app.entities;

public class TemplateEntity extends BaseEntity{
    private String template;
    private String type;
    private boolean isSingular;


    public TemplateEntity(String templates, String type) {
        this.template = templates;
        this.type = type;
    }

    public TemplateEntity() {
    }

    public boolean getIsSingular() {
        return isSingular;
    }

    public void setIsSingular(boolean singular) {
        isSingular = singular;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TemplatesEntity{" +
                "templates='" + template + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
