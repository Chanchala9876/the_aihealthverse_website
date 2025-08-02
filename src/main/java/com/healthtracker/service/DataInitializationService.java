package com.healthtracker.service;

import com.healthtracker.model.Doctor;
import com.healthtracker.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeDoctorData();
    }

    private void initializeDoctorData() {
        // Check if doctor with email jhalkumari2003@gmail.com already exists
        Optional<Doctor> existingDoctor = doctorRepository.findAll().stream()
            .filter(doctor -> "jhalkumari2003@gmail.com".equals(doctor.getEmail()))
            .findFirst();

        if (existingDoctor.isEmpty()) {
            // Create a new mental health specialist doctor
            Doctor mentalHealthDoctor = new Doctor();
            mentalHealthDoctor.setName("Dr. Jhal Kumari");
            mentalHealthDoctor.setEmail("jhalkumari2003@gmail.com");
            mentalHealthDoctor.setPassword("doctor123"); // Simple password for demo
            mentalHealthDoctor.setSpecialization("mental-health");
            mentalHealthDoctor.setQualifications("MD Psychiatry, MBBS");
            mentalHealthDoctor.setExperienceYears(8);
            mentalHealthDoctor.setBio("Experienced mental health professional specializing in anxiety, depression, and stress management. Dedicated to providing compassionate care and evidence-based treatment.");
            mentalHealthDoctor.setRating(4.8);
            mentalHealthDoctor.setConsultationFee("$80/hr");
            mentalHealthDoctor.setLanguage("English, Hindi");
            mentalHealthDoctor.setAvailable(true);
            mentalHealthDoctor.setAvailableTimeSlots(Arrays.asList(
                "09:00-10:00", "10:00-11:00", "11:00-12:00", 
                "14:00-15:00", "15:00-16:00", "16:00-17:00"
            ));
            mentalHealthDoctor.setTotalConsultations(250);
            mentalHealthDoctor.setCreatedAt(LocalDateTime.now());
            mentalHealthDoctor.setUpdatedAt(LocalDateTime.now());

            doctorRepository.save(mentalHealthDoctor);
            System.out.println("✅ Mental Health Doctor created: Dr. Jhal Kumari (jhalkumari2003@gmail.com)");
        } else {
            System.out.println("✅ Mental Health Doctor already exists: Dr. Jhal Kumari (jhalkumari2003@gmail.com)");
        }

        // Add a few more sample mental health doctors if needed
        addAdditionalDoctorsIfNeeded();
    }

    private void addAdditionalDoctorsIfNeeded() {
        long doctorCount = doctorRepository.count();
        
        if (doctorCount < 3) {
            // Add Dr. Sarah Wilson - Clinical Psychologist
            Doctor psychologist = new Doctor();
            psychologist.setName("Dr. Sarah Wilson");
            psychologist.setEmail("sarah.wilson@healthverse.com");
            psychologist.setPassword("doctor123"); // Add password
            psychologist.setSpecialization("mental-health");
            psychologist.setQualifications("PhD Clinical Psychology, MSc Psychology");
            psychologist.setExperienceYears(12);
            psychologist.setBio("Clinical psychologist specializing in cognitive behavioral therapy, trauma recovery, and relationship counseling.");
            psychologist.setRating(4.9);
            psychologist.setConsultationFee("₹1000");
            psychologist.setLanguage("English");
            psychologist.setAvailable(true);
            psychologist.setAvailableTimeSlots(Arrays.asList(
                "10:00-11:00", "11:00-12:00", "15:00-16:00", "16:00-17:00"
            ));
            psychologist.setTotalConsultations(180);
            psychologist.setCreatedAt(LocalDateTime.now());
            psychologist.setUpdatedAt(LocalDateTime.now());

            doctorRepository.save(psychologist);
            System.out.println("✅ Additional Mental Health Doctor created: Dr. Sarah Wilson");

            // Add Dr. Rajesh Kumar - Psychiatrist
            Doctor psychiatrist = new Doctor();
            psychiatrist.setName("Dr. Rajesh Kumar");
            psychiatrist.setEmail("rajesh.kumar@healthverse.com");
            psychiatrist.setPassword("doctor123"); // Add password
            psychiatrist.setSpecialization("mental-health");
            psychiatrist.setQualifications("MD Psychiatry, MBBS, Diploma in Psychiatric Medicine");
            psychiatrist.setExperienceYears(15);
            psychiatrist.setBio("Senior psychiatrist with expertise in mood disorders, anxiety management, and addiction treatment.");
            psychiatrist.setRating(4.7);
            psychiatrist.setConsultationFee("₹1200");
            psychiatrist.setLanguage("English, Hindi, Bengali");
            psychiatrist.setAvailable(true);
            psychiatrist.setAvailableTimeSlots(Arrays.asList(
                "09:00-10:00", "10:00-11:00", "14:00-15:00", "17:00-18:00"
            ));
            psychiatrist.setTotalConsultations(320);
            psychiatrist.setCreatedAt(LocalDateTime.now());
            psychiatrist.setUpdatedAt(LocalDateTime.now());

            doctorRepository.save(psychiatrist);
            System.out.println("✅ Additional Mental Health Doctor created: Dr. Rajesh Kumar");
        }
    }
}
