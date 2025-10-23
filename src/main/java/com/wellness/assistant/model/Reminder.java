package com.wellness.assistant.model;

import java.time.LocalDateTime;

/**
 * Model class representing a reminder notification.
 */
public class Reminder {
    private int id;
    private int habitId;
    private String habitName;
    private LocalDateTime scheduledTime;
    private boolean shown;
    private boolean completed;

    public Reminder() {
        this.shown = false;
        this.completed = false;
    }

    public Reminder(int habitId, String habitName, LocalDateTime scheduledTime) {
        this();
        this.habitId = habitId;
        this.habitName = habitName;
        this.scheduledTime = scheduledTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", habitId=" + habitId +
                ", habitName='" + habitName + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", shown=" + shown +
                ", completed=" + completed +
                '}';
    }
}