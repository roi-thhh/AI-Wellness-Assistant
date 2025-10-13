package com.wellness.assistant.model;

public class Habit {
    private int id;
    private String type;
    private String status;
    private String time; // New field for the reminder time

    public Habit(int id, String type, String status, String time) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.time = time;
    }

    // This controls how the habit is displayed in the list view
    @Override
    public String toString() {
        return type + " at " + time + "  -  " + status;
    }

    // Getters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getTime() { return time; }
}