package com.wellness.assistant.model;

/**
 * Model class representing a user in the wellness assistant application.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String title;
    private String motivationalMessage;

    public User() {
        this.title = "Health Champion";
        this.motivationalMessage = "Keep up the great work!";
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMotivationalMessage() {
        return motivationalMessage;
    }

    public void setMotivationalMessage(String motivationalMessage) {
        this.motivationalMessage = motivationalMessage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", motivationalMessage='" + motivationalMessage + '\'' +
                '}';
    }
}