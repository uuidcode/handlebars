package com.github.uuidcode.handlebars;

public class Model {
    private String name;

    public String getName() {
        return this.name;
    }

    public Model setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isNew() {
        return true;
    }

    public boolean isOld() {
        return false;
    }

    public String hi() {
        return "hi";
    }
}


