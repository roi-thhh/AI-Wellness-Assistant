package com.wellness.assistant.model;

import java.time.LocalTime;

/**
 * Model class representing a habit in the wellness assistant application.
 */
public class Habit {
    private int id;
    private String name;
    private String type;
    private LocalTime time;
    private String frequency;
    private boolean active;
    private String notes;

    public Habit() {
        this.active = true;
    }

    public Habit(String name, String type, LocalTime time, String frequency) {
        this();
        this.name = name;
        this.type = type;
        this.time = time;
        this.frequency = frequency;
    }

    public Habit(int id, String name, String type, LocalTime time, String frequency, boolean active) {
        this(name, type, time, frequency);
        this.id = id;
        this.active = active;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", frequency='" + frequency + '\'' +
        ", active=" + active +
        ", notes='" + notes + '\'' +
                '}';
    }
}