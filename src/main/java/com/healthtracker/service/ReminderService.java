package com.healthtracker.service;

import com.healthtracker.model.Reminder;
import com.healthtracker.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    public Reminder createReminder(Reminder reminder) {
        reminder.setCreatedAt(LocalDateTime.now());
        reminder.setCompleted(false);
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getUserReminders(String userId) {
        return reminderRepository.findByUserIdOrderByReminderTimeDesc(userId);
    }

    public List<Reminder> getActiveReminders(String userId) {
        return reminderRepository.findByUserIdAndCompletedFalseOrderByReminderTimeAsc(userId);
    }

    public Reminder markReminderCompleted(String reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId).orElse(null);
        if (reminder != null) {
            reminder.setCompleted(true);
            return reminderRepository.save(reminder);
        }
        return null;
    }

    public void deleteReminder(String reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    public List<Reminder> getUpcomingReminders(String userId, LocalDateTime start, LocalDateTime end) {
        return reminderRepository.findByUserIdAndReminderTimeBetweenOrderByReminderTimeAsc(userId, start, end);
    }
} 