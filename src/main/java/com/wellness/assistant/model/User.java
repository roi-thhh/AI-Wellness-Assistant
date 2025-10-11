package com.wellness.assistant.model;

public class User {
    private int userID;
    private String name;
    private int age;

    // Constructors, Getters, and Setters
    public User(int userID, String name, int age) {
        this.userID = userID;
        this.name = name;
        this.age = age;
    }
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public int getAge() { return age; }
}