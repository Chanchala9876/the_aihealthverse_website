package com.healthtracker.service;

import com.healthtracker.model.Consultation;
import com.healthtracker.model.Doctor;
import com.healthtracker.model.User;
import com.healthtracker.repository.DoctorRepository;
import com.healthtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(JavaMailSender mailSender, DoctorRepository doctorRepository, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    // Send consultation request notification to doctor
    public void sendConsultationRequestNotification(Consultation consultation) {
        try {
            Optional<Doctor> doctorOpt = doctorRepository.findById(consultation.getDoctorId());
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(doctor.getEmail());
                message.setSubject("New Consultation Request - HealthVerse");
                message.setText(String.format(
                    "Dear Dr. %s,\n\n" +
                    "You have received a new consultation request:\n\n" +
                    "Patient: %s\n" +
                    "Specialization: %s\n" +
                    "Concern: %s\n" +
                    "Consultation Fee: $%.2f\n" +
                    "Requested At: %s\n\n" +
                    "Please log in to your doctor dashboard to review and respond to this request:\n" +
                    "http://localhost:8090/doctor/login\n\n" +
                    "Best regards,\n" +
                    "HealthVerse Team",
                    doctor.getName(),
                    consultation.getPatientName(),
                    consultation.getSpecialization(),
                    consultation.getConcern(),
                    consultation.getConsultationFee(),
                    consultation.getRequestedAt()
                ));

                mailSender.send(message);
                logger.info("Consultation request notification sent to doctor: {}", doctor.getEmail());
            }
        } catch (Exception e) {
            logger.error("Failed to send consultation request notification", e);
        }
    }

    // Send consultation accepted notification to patient
    public void sendConsultationAcceptedNotification(Consultation consultation) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(consultation.getPatientId());
            if (userOpt.isEmpty()) {
                // If not found by email, try by ID (depending on how patientId is stored)
                userOpt = userRepository.findById(consultation.getPatientId());
            }
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Consultation Request Accepted - HealthVerse");
                message.setText(String.format(
                    "Dear %s,\n\n" +
                    "Great news! Dr. %s has accepted your consultation request.\n\n" +
                    "Consultation Details:\n" +
                    "Doctor: %s\n" +
                    "Specialization: %s\n" +
                    "Your Concern: %s\n" +
                    "Status: Active\n\n" +
                    "You can now start chatting with your doctor through your dashboard:\n" +
                    "http://localhost:8090/dashboard\n\n" +
                    "Best regards,\n" +
                    "HealthVerse Team",
                    user.getFirstName() != null ? user.getFirstName() : user.getUsername(),
                    consultation.getDoctorName(),
                    consultation.getDoctorName(),
                    consultation.getSpecialization(),
                    consultation.getConcern()
                ));

                mailSender.send(message);
                logger.info("Consultation accepted notification sent to patient: {}", user.getEmail());
            }
        } catch (Exception e) {
            logger.error("Failed to send consultation accepted notification", e);
        }
    }

    // Send consultation rejected notification to patient
    public void sendConsultationRejectedNotification(Consultation consultation, String reason) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(consultation.getPatientId());
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findById(consultation.getPatientId());
            }
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Consultation Request Update - HealthVerse");
                message.setText(String.format(
                    "Dear %s,\n\n" +
                    "We regret to inform you that Dr. %s is unable to accept your consultation request at this time.\n\n" +
                    "Consultation Details:\n" +
                    "Doctor: %s\n" +
                    "Specialization: %s\n" +
                    "Your Concern: %s\n" +
                    (reason != null && !reason.trim().isEmpty() ? "Reason: " + reason + "\n" : "") +
                    "\n" +
                    "Don't worry! You can find other available doctors in the same specialization:\n" +
                    "http://localhost:8090/mental-health/find-doctors\n\n" +
                    "Best regards,\n" +
                    "HealthVerse Team",
                    user.getFirstName() != null ? user.getFirstName() : user.getUsername(),
                    consultation.getDoctorName(),
                    consultation.getDoctorName(),
                    consultation.getSpecialization(),
                    consultation.getConcern()
                ));

                mailSender.send(message);
                logger.info("Consultation rejected notification sent to patient: {}", user.getEmail());
            }
        } catch (Exception e) {
            logger.error("Failed to send consultation rejected notification", e);
        }
    }

    // Send consultation completed notification to patient
    public void sendConsultationCompletedNotification(Consultation consultation) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(consultation.getPatientId());
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findById(consultation.getPatientId());
            }
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Consultation Completed - HealthVerse");
                message.setText(String.format(
                    "Dear %s,\n\n" +
                    "Your consultation with Dr. %s has been completed.\n\n" +
                    "Consultation Summary:\n" +
                    "Doctor: %s\n" +
                    "Specialization: %s\n" +
                    "Your Concern: %s\n" +
                    (consultation.getConsultationNotes() != null ? "Doctor's Notes: " + consultation.getConsultationNotes() + "\n" : "") +
                    (consultation.getPrescription() != null ? "Prescription: " + consultation.getPrescription() + "\n" : "") +
                    (consultation.getFollowUpInstructions() != null ? "Follow-up Instructions: " + consultation.getFollowUpInstructions() + "\n" : "") +
                    "\n" +
                    "You can view the complete consultation details in your dashboard:\n" +
                    "http://localhost:8090/dashboard\n\n" +
                    "Thank you for using HealthVerse!\n\n" +
                    "Best regards,\n" +
                    "HealthVerse Team",
                    user.getFirstName() != null ? user.getFirstName() : user.getUsername(),
                    consultation.getDoctorName(),
                    consultation.getDoctorName(),
                    consultation.getSpecialization(),
                    consultation.getConcern()
                ));

                mailSender.send(message);
                logger.info("Consultation completed notification sent to patient: {}", user.getEmail());
            }
        } catch (Exception e) {
            logger.error("Failed to send consultation completed notification", e);
        }
    }

    // Send general notification
    public void sendNotification(String toEmail, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            logger.info("Notification sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send notification to: " + toEmail, e);
        }
    }
}
