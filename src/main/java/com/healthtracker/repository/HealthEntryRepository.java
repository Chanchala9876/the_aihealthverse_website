package com.healthtracker.repository;

import com.healthtracker.model.HealthEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HealthEntryRepository extends MongoRepository<HealthEntry, String> {
    List<HealthEntry> findByUserIdOrderByEntryDateDesc(String userId);
    List<HealthEntry> findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(String userId, LocalDate startDate, LocalDate endDate);
    HealthEntry findByUserIdAndEntryDate(String userId, LocalDate entryDate);
} 