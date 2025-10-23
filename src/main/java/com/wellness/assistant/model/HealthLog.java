package com.wellness.assistant.model;

import java.time.LocalDateTime;

/**
 * Model class representing a health activity log entry.
 */
public class HealthLog {
    private int logId;
    private int habitId;
    private String habitName;
    private String status;
    private LocalDateTime timestamp;

    public HealthLog() {}

    public HealthLog(int habitId, String habitName, String status) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public HealthLog(int logId, int habitId, String habitName, String status, LocalDateTime timestamp) {
        this(habitId, habitName, status);
        this.logId = logId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HealthLog{" +
                "logId=" + logId +
                ", habitId=" + habitId +
                ", habitName='" + habitName + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
