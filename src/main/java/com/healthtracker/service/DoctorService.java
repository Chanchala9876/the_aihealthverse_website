package com.healthtracker.service;

import com.healthtracker.model.Doctor;
import com.healthtracker.model.Consultation;
import com.healthtracker.repository.DoctorRepository;
import com.healthtracker.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findByAvailable(true);
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndAvailable(specialization, true);
    }

    public Optional<Doctor> getDoctorById(String id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getTopRatedDoctors(String specialization) {
        return doctorRepository.findBySpecializationAndRatingGreaterThanEqualOrderByRatingDesc(specialization, 4.0);
    }

    public Doctor saveDoctor(Doctor doctor) {
        doctor.setUpdatedAt(LocalDateTime.now());
        return doctorRepository.save(doctor);
    }

    public Consultation requestConsultation(String patientId, String patientName, String doctorId, 
                                          String concern, String specialization) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found");
        }

        Doctor doctor = doctorOpt.get();
        // Parse fee: remove $ and /hr suffix
        String feeString = doctor.getConsultationFee()
            .replace("$", "")
            .replace("/hr", "")
            .trim();
        double fee = Double.parseDouble(feeString);
        
        Consultation consultation = new Consultation(
            patientId, patientName, doctorId, doctor.getName(), 
            specialization, concern, fee
        );

        return consultationRepository.save(consultation);
    }

    public List<Consultation> getPatientConsultations(String patientId) {
        return consultationRepository.findByPatientIdOrderByRequestedAtDesc(patientId);
    }

    public List<Consultation> getDoctorConsultations(String doctorId) {
        return consultationRepository.findByDoctorIdOrderByRequestedAtDesc(doctorId);
    }

    public Consultation updateConsultationStatus(String consultationId, String status) {
        Optional<Consultation> consultationOpt = consultationRepository.findById(consultationId);
        if (consultationOpt.isEmpty()) {
            throw new RuntimeException("Consultation not found");
        }

        Consultation consultation = consultationOpt.get();
        consultation.setStatus(status);
        
        if ("active".equals(status)) {
            consultation.setStartedAt(LocalDateTime.now());
        } else if ("completed".equals(status)) {
            consultation.setEndedAt(LocalDateTime.now());
        }

        return consultationRepository.save(consultation);
    }

    public Optional<Consultation> getConsultationById(String consultationId) {
        return consultationRepository.findById(consultationId);
    }
    
    public Consultation saveConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    // Initialize sample doctors
    public void initializeSampleDoctors() {
        if (doctorRepository.count() == 0) {
            List<Doctor> sampleDoctors = Arrays.asList(
                new Doctor("Dr. Sarah Johnson", "sarah.johnson@healthverse.com", "mental-health", 
                          "MD, Psychiatry", 8, "Experienced psychiatrist specializing in anxiety and depression treatment", 
                          4.8, "$75/hr", "English"),

                new Doctor("Dr. Michael Chen", "michael.chen@healthverse.com", "skin", 
                          "MD, Dermatology", 12, "Board-certified dermatologist with expertise in skin conditions and cosmetic treatments", 
                          4.9, "$65/hr", "English"),

                new Doctor("Dr. Emily Rodriguez", "emily.rodriguez@healthverse.com", "bones", 
                          "MD, Orthopedics", 15, "Orthopedic surgeon specializing in joint replacement and sports injuries", 
                          4.7, "$85/hr", "English"),

                new Doctor("Dr. James Wilson", "james.wilson@healthverse.com", "eyes", 
                          "MD, Ophthalmology", 10, "Eye specialist with expertise in vision correction and eye diseases", 
                          4.6, "$70/hr", "English"),

                new Doctor("Dr. Lisa Thompson", "lisa.thompson@healthverse.com", "cardiology", 
                          "MD, Cardiology", 18, "Cardiologist specializing in heart disease prevention and treatment", 
                          4.9, "$95/hr", "English"),

                new Doctor("Dr. Robert Kim", "robert.kim@healthverse.com", "general", 
                          "MD, Family Medicine", 20, "General practitioner providing comprehensive primary care services", 
                          4.5, "$55/hr", "English")
            );

            // Set additional properties for each doctor
            for (Doctor doctor : sampleDoctors) {
                doctor.setPassword("doctor123"); // Simple password for demo
                doctor.setLanguage("English");
                doctor.setAvailableTimeSlots(Arrays.asList("9:00 AM", "11:00 AM", "2:00 PM", "4:00 PM"));
                doctor.setTotalConsultations((int)(Math.random() * 500) + 100);
                doctor.setCreatedAt(LocalDateTime.now());
                doctor.setUpdatedAt(LocalDateTime.now());
            }

            doctorRepository.saveAll(sampleDoctors);
        }
    }
}
