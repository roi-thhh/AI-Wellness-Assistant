package com.wellness.assistant.storage;

import com.wellness.assistant.model.Habit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:wellness_assistant.db";
    private static DatabaseManager instance;

    // Private constructor to ensure only one instance is created (Singleton Pattern)
    private DatabaseManager() {
        initializeDb();
    }

    // Public method to get the single instance of the class
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDb() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            // SQL statement for creating a new table
            String sql = "CREATE TABLE IF NOT EXISTS habits ("
                       + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                       + " type TEXT NOT NULL,"
                       + " status TEXT NOT NULL"
                       + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public void addHabit(Habit habit) {
        String sql = "INSERT INTO habits(type, status) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, habit.getType());
            pstmt.setString(2, habit.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding habit: " + e.getMessage());
        }
    }

    public List<Habit> getAllHabits() {
        List<Habit> habits = new ArrayList<>();
        String sql = "SELECT id, type, status FROM habits";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through the result set and create Habit objects
            while (rs.next()) {
                habits.add(new Habit(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving habits: " + e.getMessage());
        }
        return habits;
    }
}