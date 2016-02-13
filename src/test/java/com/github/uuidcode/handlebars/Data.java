package com.github.uuidcode.handlebars;

public class Data {
    private String title;
    private Model model;
    private Integer age;
    
    public Integer getAge() {
        return this.age;
    }
    
    public Data setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Model getModel() {
        return this.model;
    }

    public Data setModel(Model model) {
        this.model = model;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Data setTitle(String title) {
        this.title = title;
        return this;
    }
}