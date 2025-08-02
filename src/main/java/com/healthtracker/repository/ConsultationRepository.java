package com.healthtracker.repository;

import com.healthtracker.model.Consultation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends MongoRepository<Consultation, String> {
    List<Consultation> findByPatientId(String patientId);
    List<Consultation> findByDoctorId(String doctorId);
    List<Consultation> findByPatientIdAndStatus(String patientId, String status);
    List<Consultation> findByDoctorIdAndStatus(String doctorId, String status);
    List<Consultation> findByStatus(String status);
    Optional<Consultation> findByPatientIdAndDoctorIdAndStatus(String patientId, String doctorId, String status);
    List<Consultation> findByPatientIdOrderByRequestedAtDesc(String patientId);
    List<Consultation> findByDoctorIdOrderByRequestedAtDesc(String doctorId);
}
