package com.healthtracker.repository;

import com.healthtracker.model.MentalHealthAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentalHealthAnalysisRepository extends MongoRepository<MentalHealthAnalysis, String> {
    
    // Find all analyses for a specific user
    List<MentalHealthAnalysis> findByUserId(String userId);
    
    // Find all analyses by a specific doctor
    List<MentalHealthAnalysis> findByDoctorId(String doctorId);
    
    // Find all analyses for a user with a specific doctor
    List<MentalHealthAnalysis> findByUserIdAndDoctorId(String userId, String doctorId);
    
    // Find analyses by session type (AI or Doctor consultation)
    List<MentalHealthAnalysis> findBySessionType(String sessionType);
    
    // Find analyses by session status
    List<MentalHealthAnalysis> findBySessionStatus(String sessionStatus);
    
    // Find emergency cases
    List<MentalHealthAnalysis> findByEmergencyFlagTrue();
    
    // Find high-risk assessments
    List<MentalHealthAnalysis> findByRiskAssessment(String riskAssessment);
    
    // Find analyses within a date range
    List<MentalHealthAnalysis> findBySessionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find recent analyses for a user (ordered by session date desc)
    List<MentalHealthAnalysis> findByUserIdOrderBySessionDateDesc(String userId);
    
    // Find analyses for a specific user and session type
    List<MentalHealthAnalysis> findByUserIdAndSessionType(String userId, String sessionType);
    
    // Count analyses for a specific doctor
    long countByDoctorId(String doctorId);
    
    // Count analyses for a specific user
    long countByUserId(String userId);
}
