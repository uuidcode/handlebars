package com.github.uuidcode.handlebars;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;


public class Main {
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
                .filter(line -> line.trim().length() > 0)
                .map(String::trim)
                .collect(Collectors.joining(""));

        Assert.assertEquals(expectedResult, result);
    }

    public static String hello(String to) {
        return "hello " + to;
    }

    @Test
    public void helper() throws Exception {
        Handlebars handlebars = new Handlebars();
        handlebars.registerHelpers(Main.class);
        Template template = handlebars.compileInline("{{hello title}} {{title}} {{#with model}}<h1>{{name}}</h1>{{/with}}");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Assert.assertEquals("hello Title Title <h1>TEST</h1>", template.apply(data));
    }

    @Test
    public void nest() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{title}} <h1>{{model.name}}</h1>");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Assert.assertEquals("Title <h1>TEST</h1>", template.apply(data));
    }

    @Test
    public void nestAndMethod() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{title}} <h1>{{model.getName}}</h1>");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Context context = Context
            .newBuilder(data)
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Assert.assertEquals("Title <h1>TEST</h1>", template.apply(context));
    }

    @Test
    public void nestAndMethod2() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{title}} <h1>{{model.isNew}}</h1>");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Context context = Context
            .newBuilder(data)
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Assert.assertEquals("Title <h1>true</h1>", template.apply(context));
    }

    @Test
    public void testIf() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{title}} {{#if model.isOld}}OK{{/if}}");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Assert.assertEquals("Title ", template.apply(data));
    }

    @Test
    public void testIf2() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{title}} {{#if model.isNew}}OK{{/if}}");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Context context = Context
            .newBuilder(data)
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Assert.assertEquals("Title OK", template.apply(context));
    }

    @Test
    public void map() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{time}} {{title}} {{getTitle}} {{model.name}} {{model.getName}} {{#if model.isNew}}isNew{{/if}} {{#unless model.isOld}}isOld{{/unless}}");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", data);
        map.put("time", "10");

        Context context = Context
            .newBuilder(data)
            .combine("time", "10")
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Assert.assertEquals("10 Title Title TEST TEST isNew isOld", template.apply(context));
    }

    @Test
    public void map2() throws Exception {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{model.hi}} {{model.getName}} {{time}} {{data.title}} {{data.getTitle}} {{data.model.name}} {{data.model.getName}} {{#if data.model.isNew}}isNew{{/if}} {{#unless data.model.isOld}}isOld{{/unless}}");

        Data data = this.getData();
        data.setModel(new Model().setName("TEST"));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", data);
        map.put("time", "10");
        map.put("model", new Model().setName("Hello"));

        Context context = Context
            .newBuilder(map)
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Assert.assertEquals("hi Hello 10 Title Title TEST TEST isNew isOld", template.apply(context));
    }

    @Test
    public void method() throws Exception {
        Handlebars handlebars = new Handlebars();

        Context context = Context
            .newBuilder(this.getData())
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Template template = handlebars.compileInline("Hello {{getTitle}}!");
        Assert.assertEquals("Hello Title!", template.apply(context));
    }

    @Test
    public void method2() throws Exception {
        Handlebars handlebars = new Handlebars();

        Context context = Context
            .newBuilder(this.getData())
            .resolver(
                MapValueResolver.INSTANCE,
                JavaBeanValueResolver.INSTANCE,
                FieldValueResolver.INSTANCE,
                MethodValueResolver.INSTANCE
            )
            .build();

        Template template = handlebars.compileInline("Hello {{title}}!");
        Assert.assertEquals("Hello Title!", template.apply(context));
    }

    @Test
    public void method3() throws Exception {
        Handlebars handlebars = new Handlebars();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", this.getData());

        Template template = handlebars.compileInline("Hello {{data.getTitle}}!");
        Assert.assertEquals("Hello Title!", template.apply(map));
    }
}
