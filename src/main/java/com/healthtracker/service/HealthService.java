package com.healthtracker.service;

import com.healthtracker.model.HealthEntry;
import com.healthtracker.repository.HealthEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

@Service
public class HealthService {

    @Autowired
    private HealthEntryRepository healthEntryRepository;

    @Autowired
    private AiService aiService;

    public HealthEntry saveHealthEntry(HealthEntry entry) {
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        
        // Generate AI suggestion
        String suggestion = aiService.generateWellnessTip(
            entry.getMood(),
            String.join(", ", entry.getMeals()),
            String.join(", ", entry.getSymptoms()),
            entry.getSleepHours()
        );
        entry.setAiSuggestion(suggestion);
        
        return healthEntryRepository.save(entry);
    }

    public List<HealthEntry> getUserEntries(String userId) {
        return healthEntryRepository.findByUserIdOrderByEntryDateDesc(userId);
    }

    public List<HealthEntry> getUserEntriesByDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        return healthEntryRepository.findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(userId, startDate, endDate);
    }

    public HealthEntry getEntryByDate(String userId, LocalDate date) {
        return healthEntryRepository.findByUserIdAndEntryDate(userId, date);
    }

    public String analyzeUserHealth(String userId) {
        List<HealthEntry> entries = getUserEntries(userId);
        return aiService.analyzeHealthTrends(entries);
    }

    public HealthEntry createEntryFromForm(String userId, String mood, String meals, String symptoms, int sleepHours, String notes) {
        HealthEntry entry = new HealthEntry();
        entry.setUserId(userId);
        entry.setEntryDate(LocalDate.now());
        entry.setMood(mood);
        entry.setMeals(Arrays.asList(meals.split(",")));
        entry.setSymptoms(Arrays.asList(symptoms.split(",")));
        entry.setSleepHours(sleepHours);
        entry.setNotes(notes);
        
        return saveHealthEntry(entry);
    }
} 