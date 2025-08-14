package com.healthtracker.repository;

import com.healthtracker.model.ConsultationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRequestRepository extends MongoRepository<ConsultationRequest, String> {
    
    // Find all requests for a specific doctor
    List<ConsultationRequest> findByDoctorIdOrderByRequestedAtDesc(String doctorId);
    
    // Find all requests by a specific patient
    List<ConsultationRequest> findByPatientIdOrderByRequestedAtDesc(String patientId);
    
    // Find requests by doctor email and status
    List<ConsultationRequest> findByDoctorEmailAndStatus(String doctorEmail, String status);
    
    // Find requests by doctor email
    List<ConsultationRequest> findByDoctorEmail(String doctorEmail);
    
    // Find requests by patient email
    List<ConsultationRequest> findByPatientEmail(String patientEmail);
    
    // Find requests by doctor email
    List<ConsultationRequest> findByDoctorEmailOrderByRequestedAtDesc(String doctorEmail);
    
    // Find requests by patient email
    List<ConsultationRequest> findByPatientEmailOrderByRequestedAtDesc(String patientEmail);
    
    // Find requests by status
    List<ConsultationRequest> findByStatusOrderByRequestedAtDesc(String status);
    
    // Find requests by doctor and status
    List<ConsultationRequest> findByDoctorIdAndStatusOrderByRequestedAtDesc(String doctorId, String status);
    
    // Find requests by patient and status
    List<ConsultationRequest> findByPatientIdAndStatusOrderByRequestedAtDesc(String patientId, String status);
    
    // Find request by chat ID
    Optional<ConsultationRequest> findByChatId(String chatId);
    
    // Find pending requests for a doctor
    List<ConsultationRequest> findByDoctorIdAndStatus(String doctorId, String status);
    
    // Find active requests (accepted/in-progress) for a doctor
    List<ConsultationRequest> findByDoctorIdAndStatusIn(String doctorId, List<String> statuses);
    
    // Find requests by patient email, doctor id and status
    List<ConsultationRequest> findByPatientEmailAndDoctorIdAndStatus(String patientEmail, String doctorId, String status);
}
