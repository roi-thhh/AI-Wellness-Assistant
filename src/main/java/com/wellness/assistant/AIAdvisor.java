package com.wellness.assistant;

import com.wellness.assistant.model.HealthLog;
import com.wellness.assistant.model.Habit;
import com.wellness.assistant.storage.DatabaseManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI advisor that generates recommendations based on user activity patterns.
 */
public class AIAdvisor {
    private final DatabaseManager dbManager;

    public AIAdvisor() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Generates AI-powered recommendations based on user activity patterns.
     * @return List of recommendation strings
     */
    public List<String> generateRecommendations() {
        List<String> recommendations = new ArrayList<>();
        
        try {
            List<HealthLog> logs = dbManager.getAllLogs();
            List<Habit> habits = dbManager.getAllHabits();
            
            if (logs.isEmpty()) {
                recommendations.add("Welcome to Wellness Hub! Start by completing your first habit to get personalized recommendations.");
                recommendations.add("Consistency is key to building lasting wellness habits. Try to complete at least one habit daily!");
                return recommendations;
            }

            // Analyze recent activity patterns
            analyzeRecentActivity(logs, recommendations);
            
            // Analyze habit-specific performance
            analyzeHabitPerformance(logs, habits, recommendations);
            
            // Analyze consistency patterns
            analyzeConsistency(logs, recommendations);
            
            // Analyze time-based patterns
            analyzeTimePatterns(logs, recommendations);
            
            // Analyze wellness trends
            analyzeWellnessTrends(logs, habits, recommendations);
            
            // Provide motivational insights
            provideMotivationalInsights(logs, recommendations);
            
            // If no specific recommendations, provide general encouragement
            if (recommendations.isEmpty()) {
                recommendations.add("Great job maintaining your wellness routine! Keep up the consistent effort.");
                recommendations.add("Your dedication to wellness is inspiring. Continue building these healthy habits!");
            }
            
        } catch (Exception e) {
            System.err.println("Error generating recommendations: " + e.getMessage());
            recommendations.add("Keep up the great work with your wellness journey!");
            recommendations.add("Every small step towards wellness counts. You're doing amazing!");
        }
        
        return recommendations;
    }

    private void analyzeRecentActivity(List<HealthLog> logs, List<String> recommendations) {
        // Get logs from last 7 days
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        List<HealthLog> recentLogs = logs.stream()
                .filter(log -> log.getTimestamp().isAfter(weekAgo))
                .collect(Collectors.toList());

        long completedCount = recentLogs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .count();

        long totalCount = recentLogs.size();

        if (totalCount > 0) {
            double completionRate = (double) completedCount / totalCount * 100;
            
            if (completionRate >= 80) {
                recommendations.add("You're on fire! " + completedCount + " activities completed in your last " + totalCount + " logs. You're building strong habits!");
            } else if (completionRate >= 60) {
                recommendations.add("Good progress! You've completed " + completedCount + " out of " + totalCount + " recent activities. Keep the momentum going!");
            } else if (completionRate < 40) {
                recommendations.add("Consider setting smaller, more achievable goals to build consistency in your wellness routine.");
            }
        }
    }

    private void analyzeHabitPerformance(List<HealthLog> logs, List<Habit> habits, List<String> recommendations) {
        Map<String, Long> habitCompletionCounts = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .collect(Collectors.groupingBy(
                        HealthLog::getHabitName,
                        Collectors.counting()
                ));

        // Find the most completed habit
        Optional<Map.Entry<String, Long>> topHabit = habitCompletionCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (topHabit.isPresent() && topHabit.get().getValue() >= 3) {
            String habitName = topHabit.get().getKey();
            long count = topHabit.get().getValue();
            
            // Get habit type for specific encouragement
            String habitType = habits.stream()
                    .filter(habit -> habit.getName().equals(habitName))
                    .map(Habit::getType)
                    .findFirst()
                    .orElse("activity");

            String encouragement = switch (habitType) {
                case "water" -> "Amazing work with water! You've completed it " + count + " times. Keep this momentum going!";
                case "exercise" -> "Excellent exercise consistency! You've completed " + habitName + " " + count + " times. Your body will thank you!";
                case "medicine" -> "Great job staying consistent with " + habitName + "! You've completed it " + count + " times. Health is wealth!";
                case "eye_rest" -> "Fantastic eye care routine! You've completed " + habitName + " " + count + " times. Your eyes are grateful!";
                default -> "Outstanding consistency with " + habitName + "! You've completed it " + count + " times. Keep it up!";
            };
            
            recommendations.add(encouragement);
        }
    }

    private void analyzeConsistency(List<HealthLog> logs, List<String> recommendations) {
        // Check for daily consistency
        Map<LocalDate, Long> dailyCompletions = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .collect(Collectors.groupingBy(
                        log -> log.getTimestamp().toLocalDate(),
                        Collectors.counting()
                ));

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);

        boolean completedToday = dailyCompletions.containsKey(today) && dailyCompletions.get(today) > 0;
        boolean completedYesterday = dailyCompletions.containsKey(yesterday) && dailyCompletions.get(yesterday) > 0;
        boolean completedTwoDaysAgo = dailyCompletions.containsKey(twoDaysAgo) && dailyCompletions.get(twoDaysAgo) > 0;

        if (completedToday && completedYesterday && completedTwoDaysAgo) {
            recommendations.add("Fantastic streak! You've completed activities for 3+ consecutive days. Consistency is key to building lasting habits!");
        } else if (!completedToday && completedYesterday) {
            recommendations.add("You did great yesterday! Don't forget to complete today's activities to maintain your wellness momentum.");
        }
    }

    private void analyzeTimePatterns(List<HealthLog> logs, List<String> recommendations) {
        // Analyze completion times to suggest optimal timing
        Map<Integer, Long> hourCompletions = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .collect(Collectors.groupingBy(
                        log -> log.getTimestamp().getHour(),
                        Collectors.counting()
                ));

        // Find the hour with most completions
        Optional<Map.Entry<Integer, Long>> bestHour = hourCompletions.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (bestHour.isPresent() && bestHour.get().getValue() >= 3) {
            int hour = bestHour.get().getKey();
            long count = bestHour.get().getValue();
            
            String timeDescription = getTimeDescription(hour);
            recommendations.add("You're most productive at " + timeDescription + "! You've completed " + count + " activities during this time. Consider scheduling more habits around this period.");
        }
    }

    private String getTimeDescription(int hour) {
        return switch (hour) {
            case 6, 7, 8 -> "morning (6-8 AM)";
            case 9, 10, 11 -> "late morning (9-11 AM)";
            case 12, 13, 14 -> "afternoon (12-2 PM)";
            case 15, 16, 17 -> "late afternoon (3-5 PM)";
            case 18, 19, 20 -> "evening (6-8 PM)";
            case 21, 22, 23 -> "night (9-11 PM)";
            default -> "early morning";
        };
    }

    private void analyzeWellnessTrends(List<HealthLog> logs, List<Habit> habits, List<String> recommendations) {
        // Analyze wellness balance
        Map<String, Long> typeCompletionCounts = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .collect(Collectors.groupingBy(
                        HealthLog::getHabitName,
                        Collectors.counting()
                ));

        // Check for wellness balance
        long waterCount = typeCompletionCounts.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("water") || 
                                entry.getKey().toLowerCase().contains("hydration"))
                .mapToLong(Map.Entry::getValue)
                .sum();

        long exerciseCount = typeCompletionCounts.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("exercise") || 
                                entry.getKey().toLowerCase().contains("walk") ||
                                entry.getKey().toLowerCase().contains("workout"))
                .mapToLong(Map.Entry::getValue)
                .sum();

        long medicineCount = typeCompletionCounts.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("medicine") || 
                                entry.getKey().toLowerCase().contains("vitamin"))
                .mapToLong(Map.Entry::getValue)
                .sum();

        // Provide wellness balance insights
        if (waterCount > 0 && exerciseCount == 0) {
            recommendations.add("Great job staying hydrated! Consider adding some physical activity to balance your wellness routine.");
        } else if (exerciseCount > 0 && waterCount == 0) {
            recommendations.add("Excellent work with exercise! Remember to stay hydrated throughout your active lifestyle.");
        } else if (waterCount > 0 && exerciseCount > 0) {
            recommendations.add("Perfect wellness balance! You're covering both hydration and physical activity - keep it up!");
        }
    }

    private void provideMotivationalInsights(List<HealthLog> logs, List<String> recommendations) {
        // Calculate total completed activities
        long totalCompleted = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .count();

        // Provide milestone-based motivation
        if (totalCompleted >= 50) {
            recommendations.add("🎉 Amazing milestone! You've completed 50+ activities. You're a true wellness champion!");
        } else if (totalCompleted >= 25) {
            recommendations.add("🌟 Fantastic progress! You're halfway to 50 activities. Your dedication is inspiring!");
        } else if (totalCompleted >= 10) {
            recommendations.add("⭐ Great start! You've completed 10+ activities. You're building strong wellness habits!");
        }

        // Analyze improvement trends
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastWeek = now.minusDays(7);
        LocalDateTime twoWeeksAgo = now.minusDays(14);

        long recentWeekCompletions = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .filter(log -> log.getTimestamp().isAfter(lastWeek))
                .count();

        long previousWeekCompletions = logs.stream()
                .filter(log -> "Completed".equals(log.getStatus()))
                .filter(log -> log.getTimestamp().isAfter(twoWeeksAgo))
                .filter(log -> log.getTimestamp().isBefore(lastWeek))
                .count();

        if (recentWeekCompletions > previousWeekCompletions && previousWeekCompletions > 0) {
            recommendations.add("📈 You're improving! This week you completed more activities than last week. Keep the momentum going!");
        }
    }

    /**
     * Generates a motivational quote for the day.
     * @return A motivational quote string
     */
    public String getDailyQuote() {
        String[] quotes = {
            "Health is not valued until sickness comes.",
            "The groundwork for all happiness is good health.",
            "Take care of your body. It's the only place you have to live.",
            "A healthy outside starts from the inside.",
            "Your body hears everything your mind says. Stay positive!",
            "Wellness is not a destination, it's a way of life.",
            "Small steps every day lead to big changes over time.",
            "Invest in your health today, thank yourself tomorrow."
        };
        
        Random random = new Random();
        return quotes[random.nextInt(quotes.length)];
    }
}
