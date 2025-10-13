package com.wellness.assistant.model;

import java.time.LocalTime;

public class Reminder {
    private int id;
    private int habitId;
    private String habitType;
    private LocalTime time; // Using LocalTime for time-of-day reminders

    public Reminder(int id, int habitId, String habitType, LocalTime time) {
        this.id = id;
        this.habitId = habitId;
        this.habitType = habitType;
        this.time = time;
    }

    // Getters and Setters
    public int getId() { return id; }
    public int getHabitId() { return habitId; }
    public String getHabitType() { return habitType; }
    public LocalTime getTime() { return time; }
}