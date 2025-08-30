package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Objects;

@Document(collection = "health_entries")
public class HealthEntry {
    @Id
    private String id;
    private String userId;
    private LocalDate entryDate;
    private String mood;
    private List<String> meals = new ArrayList<>();
    private List<String> symptoms = new ArrayList<>();
    private int sleepHours;
    private String notes;
    private String aiSuggestion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HealthEntry() {
        this.entryDate = LocalDate.now();
    }

    public HealthEntry(String id, String userId, LocalDate entryDate, String mood, List<String> meals, List<String> symptoms, int sleepHours, String notes, String aiSuggestion, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.entryDate = entryDate;
        this.mood = mood;
        this.meals = meals;
        this.symptoms = symptoms;
        this.sleepHours = sleepHours;
        this.notes = notes;
        this.aiSuggestion = aiSuggestion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public List<String> getMeals() { return meals; }
    public void setMeals(List<String> meals) { this.meals = meals; }
    public List<String> getSymptoms() { return symptoms; }
    public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }
    public int getSleepHours() { return sleepHours; }
    public void setSleepHours(int sleepHours) { this.sleepHours = sleepHours; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getAiSuggestion() { return aiSuggestion; }
    public void setAiSuggestion(String aiSuggestion) { this.aiSuggestion = aiSuggestion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "HealthEntry{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", entryDate=" + entryDate +
                ", mood='" + mood + '\'' +
                ", meals=" + meals +
                ", symptoms=" + symptoms +
                ", sleepHours=" + sleepHours +
                ", notes='" + notes + '\'' +
                ", aiSuggestion='" + aiSuggestion + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthEntry that = (HealthEntry) o;
        return sleepHours == that.sleepHours &&
                Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(entryDate, that.entryDate) &&
                Objects.equals(mood, that.mood) &&
                Objects.equals(meals, that.meals) &&
                Objects.equals(symptoms, that.symptoms) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(aiSuggestion, that.aiSuggestion) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, entryDate, mood, meals, symptoms, sleepHours, notes, aiSuggestion, createdAt, updatedAt);
    }
} 