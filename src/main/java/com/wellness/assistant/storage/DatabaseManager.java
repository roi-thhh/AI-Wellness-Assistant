package com.wellness.assistant.storage;

import com.wellness.assistant.model.Habit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:wellness_assistant.db";
    private static DatabaseManager instance;

    private DatabaseManager() {
        initializeDb();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDb() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // Updated SQL to include the 'time' column
            String sql = "CREATE TABLE IF NOT EXISTS habits ("
                       + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                       + " type TEXT NOT NULL,"
                       + " status TEXT NOT NULL,"
                       + " time TEXT" // Added time column
                       + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public void addHabit(Habit habit) {
        // Updated SQL to insert the time
        String sql = "INSERT INTO habits(type, status, time) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, habit.getType());
            pstmt.setString(2, habit.getStatus());
            pstmt.setString(3, habit.getTime()); // Save the time
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding habit: " + e.getMessage());
        }
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = new ArrayList<>();
        // Updated SQL to select the time
        String sql = "SELECT id, type, status, time FROM habits";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                habits.add(new Habit(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("status"),
                    rs.getString("time") // Load the time
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving habits: " + e.getMessage());
        }
        return habits;
    }
}