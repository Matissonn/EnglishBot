package com.MatOs;

import java.util.List;

public class Task{
    private int id;
    private String type;
    private String text;
    private List<String> options;
    private String explanation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "Task{" +
                "\nid=" + id +
                "\ntype='" + type + '\'' +
                "\ntext='" + text + '\'' +
                ", \noptions=" + options.toString() +
                ", \nexplanation='" + explanation + '\'' +
                "\n}";
    }
}