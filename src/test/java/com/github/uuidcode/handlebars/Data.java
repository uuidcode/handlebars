package com.github.uuidcode.handlebars;

public class Data {
    private String title;
    private Model model;

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