package com.github.uuidcode.handlebars;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;


public class Main {
    class Data {
        private String title;

        public String getTitle() {
            return this.title;
        }

        public Data setTitle(String title) {
            this.title = title;
            return this;
        }
    }

    public Data getData() {
        return new Data().setTitle("Title");
    }

    @Test
    public void compileInline() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hello {{this}}!");
        String to = "Handlebars.java";
        Assert.assertEquals("Hello " + to + "!", template.apply(to));
    }

    @Test
    public void escape() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hello {{this}}!");
        String to = "<html>";
        Assert.assertEquals("Hello " + StringEscapeUtils.escapeHtml4(to) + "!", template.apply(to));
    }

    @Test
    public void unescape() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hello {{{this}}}!");
        String to = "<html>";
        Assert.assertEquals("Hello " + to + "!", template.apply(to));
    }

    @Test
    public void compile() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compile("mytemplate");
        String to = "Handlebars.java";
        Assert.assertEquals("Hello " + to + "!", template.apply(to));
    }

    @Test
    public void prefixAndSuffix() throws Exception {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix("/templates");
        templateLoader.setSuffix(".html");

        Handlebars handlebars = new Handlebars(templateLoader);

        Template template = handlebars.compile("mytemplate");
        String to = "Handlebars.java";
        Assert.assertEquals("Hello " + to + "!", template.apply(to));
    }

    @Test
    public void partials() throws Exception {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix("/templates");
        templateLoader.setSuffix(".hbs");

        Handlebars handlebars = new Handlebars(templateLoader);

        Template template = handlebars.compile("home");

        String lineSeparator = System.getProperty("line.separator");

        String result =
            resultList()
                .collect(Collectors.joining(lineSeparator));

        Assert.assertEquals(result, template.apply(this.getData()));
    }

    private Stream<String> resultList() {
        return Stream.of("<h1>Title</h1>", "<p>Home page</p>", "<span>Powered by Handlebars.java</span>");
    }

    @Test
    public void inheritance() throws Exception {
        TemplateLoader templateLoader = new ClassPathTemplateLoader();
        templateLoader.setPrefix("/templates");
        templateLoader.setSuffix(".hbs");

        Handlebars handlebars = new Handlebars(templateLoader);

        Template template = handlebars.compile("inheritance_home");

        String lineSeparator = System.getProperty("line.separator");

        String expectedResult =
            resultList()
                .collect(Collectors.joining(""));

        String result = template.apply(this.getData());

        result =
            Stream.of(result.split(lineSeparator))
                .filter(
                    line -> {
                        return line.trim().length() > 0;
                    }
                ).map(
                    line -> {
                        return line.trim();
                    }
                ).collect(Collectors.joining(""));

        Assert.assertEquals(expectedResult, result);
    }
}
