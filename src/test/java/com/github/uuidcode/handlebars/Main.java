package com.github.uuidcode.handlebars;

import org.junit.Assert;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;


public class Main {
    @Test
    public void compileInline() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hello {{this}}!");
        String to = "Handlebars.java";
        Assert.assertEquals("Hello " + to + "!", template.apply(to));
    }

    @Test
    public void compile() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile("mytemplate");
        String to = "Handlebars.java";
        Assert.assertEquals("Hello " + to + "!", template.apply(to));
    }
}
