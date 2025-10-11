package com.wellness.assistant.model;

// Represents a health habit like drinking water or exercise [cite: 17]
public class Habit {
    private int id;
    private String type;
    private String status; // "Active", "Completed Today" etc.

    public Habit(int id, String type, String status) {
        this.id = id;
        this.type = type;
        this.status = status;
    }

    @Override
    public String toString() {
        return type + " - " + status; // How it will appear in a list
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getStatus() { return status; }
}