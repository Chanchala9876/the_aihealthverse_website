package com.healthtracker.repository;

import com.healthtracker.model.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends MongoRepository<Reminder, String> {
    List<Reminder> findByUserIdOrderByReminderTimeDesc(String userId);
    List<Reminder> findByUserIdAndCompletedFalseOrderByReminderTimeAsc(String userId);
    List<Reminder> findByUserIdAndReminderTimeBetweenOrderByReminderTimeAsc(String userId, LocalDateTime start, LocalDateTime end);
} 