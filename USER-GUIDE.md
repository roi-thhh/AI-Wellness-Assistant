# Wellness Hub - User Guide

## Getting Started

Welcome to Wellness Hub, your personal AI-powered health and wellness companion!

## Quick Start

1. **Run the Application**
   - Double-click `run.bat` (Windows)
   - Or run `mvn javafx:run` from the command line

2. **First Launch**
   - The app will create a `wellness_hub.db` database file
   - Sample habits will be added automatically
   - You'll see the Dashboard view by default

## Main Features

### 🏠 Dashboard

The Dashboard is your home screen showing:

- **Welcome Section**: Current date and quick greeting
- **Summary Cards**:
  - Today's Progress: How many habits you've completed today
  - Active Habits: Total number of active habits
  - Completion Rate: Your overall success percentage
  - Total Logs: Activities logged today
- **Today's Habits**: List of habits scheduled for today
- **Daily Quote**: AI-generated wellness inspiration

**Actions:**
- Click **"Done"** on any habit to mark it complete
- Click **"✏"** (Edit) to modify a habit
- Click **"+ Add New Habit"** to create a new habit

### 📋 My Habits

Manage all your habits in one place:

**Features:**
- View all your habits in a clean list
- Filter by Type: Water, Exercise, Medicine, Eye Rest, etc.
- Filter by Status: Active or Paused
- Each habit shows:
  - Icon (color-coded by type)
  - Habit name
  - Scheduled time
  - Frequency (daily, weekdays, etc.)

**Actions:**
- Click **"Done"** to complete a habit
- Click **"✏"** to edit habit details
- Click **"+ Add New Habit"** to create a new habit

### 📊 Analytics

Track your progress and get insights:

**Time Periods:**
- Today
- This Week
- Last 30 Days
- All Time

**Metrics:**
- Total Activities: All habits tracked
- Completed: Successfully done
- Skipped: Missed opportunities

**AI Insights:**
- Personalized recommendations
- Wellness trend analysis
- Activity history

## Managing Habits

### Adding a New Habit

1. Click **"+ Add New Habit"** button
2. Fill in the form:
   - **Habit Name**: e.g., "Morning Walk"
   - **Type**: Choose from dropdown (water, exercise, medicine, etc.)
   - **Time**: Enter in HH:MM format (e.g., 08:00)
   - **Frequency**: Choose daily, weekdays, weekends, or weekly
3. Click **"Save"**

### Editing a Habit

1. Click the **"✏"** (Edit) button on any habit card
2. Modify the fields you want to change
3. Click **"Save"** to update

### Marking a Habit as Done

1. Click the **"Done"** button on any habit
2. The habit will be logged as completed
3. Your progress will update automatically

## Navigation

### Sidebar Menu

The left sidebar provides quick navigation:

- **Dashboard** 🏠: Your daily overview
- **My Habits** 🎯: Manage all habits
- **Analytics** 📊: View insights and trends

**Quick Stats:**
- Shows today's progress at a glance
- Updates automatically when you complete habits

### Active Section Highlight

The currently active section is highlighted in blue-green gradient.

## Tips for Success

### 🎯 Best Practices

1. **Start Small**: Add 2-3 habits initially
2. **Be Specific**: Use clear names like "Drink water at 8 AM"
3. **Set Realistic Times**: Choose times that fit your schedule
4. **Check Daily**: Review your dashboard each morning
5. **Track Consistently**: Mark habits done as you complete them

### 💡 Habit Types

- **Water** 💧: Hydration reminders
- **Exercise** 🏃: Physical activity
- **Medicine** 💊: Medication tracking
- **Eye Rest** 👁: Screen break reminders
- **Nutrition** 🥗: Meal planning
- **Sleep** 😴: Sleep schedule
- **Mindfulness** 🧘: Meditation/relaxation

### ⏰ Time Format

Always use 24-hour format (HH:MM):
- Good: `08:00`, `14:30`, `20:00`
- Bad: `8 AM`, `2:30pm`, `eight o'clock`

### 📅 Frequency Options

- **Daily**: Every day
- **Weekdays**: Monday through Friday
- **Weekends**: Saturday and Sunday
- **Weekly**: Once per week

## Understanding Your Data

### Progress Tracking

- **Today's Progress**: Completed habits / Total active habits
- **Completion Rate**: Percentage of all habits completed over time
- **Total Logs**: Number of activities logged today

### Analytics Insights

The Analytics view shows:
- Long-term trends
- Success patterns
- Areas for improvement
- AI-powered recommendations

## Troubleshooting

### Common Issues

**Problem**: Can't add a new habit
- **Solution**: Make sure all required fields are filled
- **Check**: Time format is correct (HH:MM)

**Problem**: Navigation not working
- **Solution**: Make sure you're clicking the buttons in the sidebar
- **Check**: Look for the active highlight to confirm navigation

**Problem**: Data not saving
- **Solution**: Check that `wellness_hub.db` can be created in the project folder
- **Check**: Ensure you have write permissions

## Keyboard Shortcuts

Currently, the app uses mouse/touch navigation. Future versions may include keyboard shortcuts.

## Data & Privacy

- All data is stored locally in `wellness_hub.db`
- No internet connection required
- No data is sent to external servers
- You have complete control over your data

## Support

### Getting Help

1. Check this user guide
2. Review the `APP-READY.md` for technical details
3. Check the `PROJECT_GOALS.md` for feature overview

## Updates & Maintenance

### Database Location

The database file `wellness_hub.db` is created in:
```
AI-Wellness-Assistant/wellness_hub.db
```

### Backup Your Data

To backup your habits and logs:
1. Close the application
2. Copy `wellness_hub.db` to a safe location
3. To restore, replace the file with your backup

## Advanced Features

### AI Recommendations

The AI Advisor analyzes your:
- Completion patterns
- Habit types
- Time preferences
- Activity history

And provides:
- Personalized suggestions
- Wellness insights
- Motivation quotes

### Reminder Service

(Future enhancement)
- Background notifications
- Time-based reminders
- Custom alert sounds

## Glossary

- **Habit**: A recurring health activity you want to track
- **Log**: A record of completing a habit
- **Active Habit**: A habit that's currently being tracked
- **Completion Rate**: Percentage of habits successfully completed
- **Quick Stats**: Summary metrics shown in the sidebar

## Best Practices Summary

✅ **DO:**
- Add realistic habits you can maintain
- Check your dashboard daily
- Mark habits done immediately after completing
- Review analytics weekly
- Adjust habits as needed

❌ **DON'T:**
- Add too many habits at once
- Forget to mark completed habits
- Use vague habit names
- Ignore the analytics insights

## Success Tips

1. **Morning Routine**: Check Dashboard first thing
2. **Evening Review**: Mark any missed habits
3. **Weekly Analysis**: Review Analytics every Sunday
4. **Monthly Adjustment**: Update habits that aren't working

---

**Enjoy your wellness journey! 🌟**

For questions or issues, refer to the technical documentation in `APP-READY.md`.
