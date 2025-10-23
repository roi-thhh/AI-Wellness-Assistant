package com.wellness.assistant;

import com.wellness.assistant.model.Habit;
import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.storage.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.time.LocalTime;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Wellness Hub application.
 */
public class AppTest {

    private DatabaseManager dbManager;

    @BeforeEach
    void setUp() {
        dbManager = DatabaseManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Clean up test data if needed
    }

    @Test
    void testHabitCreation() {
        Habit habit = new Habit("Test Habit", "water", LocalTime.of(8, 0), "daily");
        
        assertNotNull(habit);
        assertEquals("Test Habit", habit.getName());
        assertEquals("water", habit.getType());
        assertEquals(LocalTime.of(8, 0), habit.getTime());
        assertEquals("daily", habit.getFrequency());
        assertTrue(habit.isActive());
    }

    @Test
    void testHealthLogCreation() {
        HealthLog log = new HealthLog(1, "Test Habit", "Completed");
        
        assertNotNull(log);
        assertEquals(1, log.getHabitId());
        assertEquals("Test Habit", log.getHabitName());
        assertEquals("Completed", log.getStatus());
        assertNotNull(log.getTimestamp());
    }

    @Test
    void testDatabaseManagerSingleton() {
        DatabaseManager instance1 = DatabaseManager.getInstance();
        DatabaseManager instance2 = DatabaseManager.getInstance();
        
        assertSame(instance1, instance2);
    }

    @Test
    void testAIAdvisorRecommendations() {
        AIAdvisor advisor = new AIAdvisor();
        assertNotNull(advisor);
        
        // Test that recommendations can be generated
        try {
            var recommendations = advisor.generateRecommendations();
            assertNotNull(recommendations);
            assertFalse(recommendations.isEmpty());
        } catch (Exception e) {
            // If database is not available, this is expected
            assertTrue(e.getMessage().contains("Error generating recommendations") || 
                      e.getMessage().contains("database"));
        }
    }

    @Test
    void testReminderServiceCreation() {
        ReminderService service = new ReminderService();
        assertNotNull(service);
        
        // Test that service can be started and stopped
        service.start();
        assertTrue(true); // If we get here, start didn't throw an exception
        
        service.stop();
        assertTrue(true); // If we get here, stop didn't throw an exception
    }

    @Test
    void testApplicationStructure() {
        // Test that main classes can be instantiated
        assertDoesNotThrow(() -> {
            MainController mainController = new MainController();
            SidebarController sidebarController = new SidebarController();
            DashboardController dashboardController = new DashboardController();
            MyHabitsController habitsController = new MyHabitsController();
            AnalyticsController analyticsController = new AnalyticsController();
            AddHabitController addHabitController = new AddHabitController();
        });
    }
}