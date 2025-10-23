package com.wellness.assistant.storage;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.model.HealthLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage SQLite database operations.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:wellness_hub.db";
    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            insertSampleData();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        // Create habits table
        String createHabitsTable = """
            CREATE TABLE IF NOT EXISTS habits (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                time TEXT NOT NULL,
                frequency TEXT NOT NULL,
                active BOOLEAN DEFAULT TRUE
            )
        """;

        // Create health_logs table
        String createLogsTable = """
            CREATE TABLE IF NOT EXISTS health_logs (
                log_id INTEGER PRIMARY KEY AUTOINCREMENT,
                habit_id INTEGER NOT NULL,
                habit_name TEXT NOT NULL,
                status TEXT NOT NULL,
                timestamp TEXT NOT NULL,
                FOREIGN KEY (habit_id) REFERENCES habits (id)
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createHabitsTable);
            stmt.execute(createLogsTable);
        }

        // Schema migration: add notes column to habits if missing
        ensureNotesColumn();
    }

    private void ensureNotesColumn() {
        try (Statement stmt = connection.createStatement()) {
            // Attempt to add the column; if it exists, SQLite will throw an error which we ignore
            stmt.executeUpdate("ALTER TABLE habits ADD COLUMN notes TEXT");
        } catch (SQLException ignored) {
            // Column likely already exists; ignore
        }
    }

    private void insertSampleData() throws SQLException {
        // Check if data already exists
        String checkData = "SELECT COUNT(*) FROM habits";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkData)) {
            if (rs.getInt(1) > 0) {
                return; // Data already exists
            }
        }

        // Insert sample habits
        String insertHabits = """
            INSERT INTO habits (name, type, time, frequency, active) VALUES
            ('Morning Hydration', 'water', '08:00', 'daily', TRUE),
            ('Evening Walk', 'exercise', '18:00', 'daily', TRUE),
            ('Vitamins', 'medicine', '09:00', 'daily', TRUE),
            ('Screen Break', 'eye_rest', '14:00', 'weekdays', TRUE)
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(insertHabits);
        }
    }

    // Habit CRUD operations
    public void addHabit(Habit habit) throws SQLException {
        // Include notes if column exists; using explicit column list keeps backward compatibility
        String sql = "INSERT INTO habits (name, type, time, frequency, active, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, habit.getName());
            pstmt.setString(2, habit.getType());
            pstmt.setString(3, habit.getTime().toString());
            pstmt.setString(4, habit.getFrequency());
            pstmt.setBoolean(5, habit.isActive());
            pstmt.setString(6, habit.getNotes());
            try {
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                // Fallback for old schema without notes
                String legacy = "INSERT INTO habits (name, type, time, frequency, active) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps2 = connection.prepareStatement(legacy)) {
                    ps2.setString(1, habit.getName());
                    ps2.setString(2, habit.getType());
                    ps2.setString(3, habit.getTime().toString());
                    ps2.setString(4, habit.getFrequency());
                    ps2.setBoolean(5, habit.isActive());
                    ps2.executeUpdate();
                }
            }
        }
    }

    public void updateHabit(Habit habit) throws SQLException {
        String sql = "UPDATE habits SET name = ?, type = ?, time = ?, frequency = ?, active = ?, notes = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, habit.getName());
            pstmt.setString(2, habit.getType());
            pstmt.setString(3, habit.getTime().toString());
            pstmt.setString(4, habit.getFrequency());
            pstmt.setBoolean(5, habit.isActive());
            pstmt.setString(6, habit.getNotes());
            pstmt.setInt(7, habit.getId());
            try {
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                // Fallback for old schema without notes
                String legacy = "UPDATE habits SET name = ?, type = ?, time = ?, frequency = ?, active = ? WHERE id = ?";
                try (PreparedStatement ps2 = connection.prepareStatement(legacy)) {
                    ps2.setString(1, habit.getName());
                    ps2.setString(2, habit.getType());
                    ps2.setString(3, habit.getTime().toString());
                    ps2.setString(4, habit.getFrequency());
                    ps2.setBoolean(5, habit.isActive());
                    ps2.setInt(6, habit.getId());
                    ps2.executeUpdate();
                }
            }
        }
    }

    public void deleteHabit(int habitId) throws SQLException {
        String sql = "DELETE FROM habits WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, habitId);
            pstmt.executeUpdate();
        }
    }

    public List<Habit> getAllHabits() throws SQLException {
        List<Habit> habits = new ArrayList<>();
    String sql = "SELECT * FROM habits ORDER BY time";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Habit habit = new Habit();
                habit.setId(rs.getInt("id"));
                habit.setName(rs.getString("name"));
                habit.setType(rs.getString("type"));
                habit.setTime(LocalTime.parse(rs.getString("time")));
                habit.setFrequency(rs.getString("frequency"));
                habit.setActive(rs.getBoolean("active"));
                try {
                    habit.setNotes(rs.getString("notes"));
                } catch (SQLException ignore) {
                    habit.setNotes(null);
                }
                habits.add(habit);
            }
        }
        return habits;
    }

    public Habit getHabitById(int id) throws SQLException {
        String sql = "SELECT * FROM habits WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Habit habit = new Habit();
                habit.setId(rs.getInt("id"));
                habit.setName(rs.getString("name"));
                habit.setType(rs.getString("type"));
                habit.setTime(LocalTime.parse(rs.getString("time")));
                habit.setFrequency(rs.getString("frequency"));
                habit.setActive(rs.getBoolean("active"));
                try { habit.setNotes(rs.getString("notes")); } catch (SQLException ignore) { habit.setNotes(null);} 
                return habit;
            }
        }
        return null;
    }

    // Health Log operations
    public void logActivity(HealthLog log) throws SQLException {
        String sql = "INSERT INTO health_logs (habit_id, habit_name, status, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, log.getHabitId());
            pstmt.setString(2, log.getHabitName());
            pstmt.setString(3, log.getStatus());
            pstmt.setString(4, log.getTimestamp().toString());
            pstmt.executeUpdate();
        }
    }

    public List<HealthLog> getAllLogs() throws SQLException {
        List<HealthLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM health_logs ORDER BY timestamp DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                HealthLog log = new HealthLog();
                log.setLogId(rs.getInt("log_id"));
                log.setHabitId(rs.getInt("habit_id"));
                log.setHabitName(rs.getString("habit_name"));
                log.setStatus(rs.getString("status"));
                log.setTimestamp(LocalDateTime.parse(rs.getString("timestamp")));
                logs.add(log);
            }
        }
        return logs;
    }

    public List<HealthLog> getLogsByHabitId(int habitId) throws SQLException {
        List<HealthLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM health_logs WHERE habit_id = ? ORDER BY timestamp DESC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, habitId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                HealthLog log = new HealthLog();
                log.setLogId(rs.getInt("log_id"));
                log.setHabitId(rs.getInt("habit_id"));
                log.setHabitName(rs.getString("habit_name"));
                log.setStatus(rs.getString("status"));
                log.setTimestamp(LocalDateTime.parse(rs.getString("timestamp")));
                logs.add(log);
            }
        }
        return logs;
    }

    // Statistics methods
    public int getTotalActiveHabits() throws SQLException {
        String sql = "SELECT COUNT(*) FROM habits WHERE active = TRUE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        }
    }

    public int getTodaysCompletedActivities() throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM health_logs 
            WHERE status = 'Completed' AND DATE(timestamp) = DATE('now')
        """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        }
    }

    public double getCompletionRate() throws SQLException {
        String sql = """
            SELECT 
                (SELECT COUNT(*) FROM health_logs WHERE status = 'Completed') * 100.0 /
                (SELECT COUNT(*) FROM health_logs)
        """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getDouble(1);
        }
    }

    // Date-range statistics
    public int getLogsCountBetween(LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT COUNT(*) FROM health_logs WHERE DATE(timestamp) BETWEEN DATE(?) AND DATE(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, start.toString());
            ps.setString(2, end.toString());
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1);
        }
    }

    public int getLogsCountByStatusBetween(String status, LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT COUNT(*) FROM health_logs WHERE status = ? AND DATE(timestamp) BETWEEN DATE(?) AND DATE(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, start.toString());
            ps.setString(3, end.toString());
            ResultSet rs = ps.executeQuery();
            return rs.getInt(1);
        }
    }

    public double getCompletionRateBetween(LocalDate start, LocalDate end) throws SQLException {
        int total = getLogsCountBetween(start, end);
        if (total == 0) return 0.0;
        int completed = getLogsCountByStatusBetween("Completed", start, end);
        return (completed * 100.0) / total;
    }

    public List<HealthLog> getLogsBetween(LocalDate start, LocalDate end) throws SQLException {
        List<HealthLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM health_logs WHERE DATE(timestamp) BETWEEN DATE(?) AND DATE(?) ORDER BY timestamp DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, start.toString());
            ps.setString(2, end.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HealthLog log = new HealthLog();
                log.setLogId(rs.getInt("log_id"));
                log.setHabitId(rs.getInt("habit_id"));
                log.setHabitName(rs.getString("habit_name"));
                log.setStatus(rs.getString("status"));
                log.setTimestamp(LocalDateTime.parse(rs.getString("timestamp")));
                logs.add(log);
            }
        }
        return logs;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}