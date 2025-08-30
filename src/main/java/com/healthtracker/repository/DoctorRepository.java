package com.healthtracker.repository;

import com.healthtracker.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByName(String name);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findBySpecializationAndAvailable(String specialization, boolean available);
    List<Doctor> findByAvailable(boolean available);
    List<Doctor> findByRatingGreaterThanEqual(double rating);
    List<Doctor> findBySpecializationAndRatingGreaterThanEqualOrderByRatingDesc(String specialization, double rating);
}
