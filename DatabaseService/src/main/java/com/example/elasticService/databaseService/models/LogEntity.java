package com.example.elasticService.databaseService.models;

public class LogEntity {

    private String message;
    private String level;

    // Constructors, Getters, Setters
    public LogEntity() {}

    public LogEntity(String message, String level) {
        this.message = message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}