package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "reminders")
public class Reminder {
    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private LocalDateTime reminderTime;
    private boolean completed;
    private String type; // MEDICATION, EXERCISE, APPOINTMENT, etc.
    private LocalDateTime createdAt;

    public Reminder() {}

    public Reminder(String id, String userId, String title, String description, LocalDateTime reminderTime, boolean completed, String type, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.reminderTime = reminderTime;
        this.completed = completed;
        this.type = type;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalDateTime reminderTime) { this.reminderTime = reminderTime; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Reminder{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", reminderTime=" + reminderTime +
                ", completed=" + completed +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return completed == reminder.completed &&
                Objects.equals(id, reminder.id) &&
                Objects.equals(userId, reminder.userId) &&
                Objects.equals(title, reminder.title) &&
                Objects.equals(description, reminder.description) &&
                Objects.equals(reminderTime, reminder.reminderTime) &&
                Objects.equals(type, reminder.type) &&
                Objects.equals(createdAt, reminder.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, description, reminderTime, completed, type, createdAt);
    }
} 