package com.healthtracker.service;

import com.healthtracker.model.ConsultationRequest;
import com.healthtracker.repository.ConsultationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {
    private static final Logger logger = LoggerFactory.getLogger(ConsultationService.class);
    
    @Autowired
    private ConsultationRequestRepository consultationRequestRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void updateConsultationStatus(String chatId, String status) {
        try {
            logger.info("Updating consultation status for chatId: {} to: {}", chatId, status);
            
            Optional<ConsultationRequest> requestOpt = consultationRequestRepository.findByChatId(chatId);
            if (requestOpt.isPresent()) {
                ConsultationRequest request = requestOpt.get();
                request.setStatus(status);
                
                if ("IN_PROGRESS".equals(status) || "COMPLETED".equals(status)) {
                    request.setRespondedAt(LocalDateTime.now());
                }
                
                ConsultationRequest savedRequest = consultationRequestRepository.save(request);
                logger.info("Updated consultation status successfully for chatId: {}", chatId);
                
                // Send real-time updates
                try {
                    // Send to doctor
                    messagingTemplate.convertAndSendToUser(
                        savedRequest.getDoctorEmail(),
                        "/queue/consultation-updates",
                        savedRequest
                    );
                    logger.debug("Sent update to doctor: {}", savedRequest.getDoctorEmail());
                    
                    // Send to patient
                    messagingTemplate.convertAndSendToUser(
                        savedRequest.getPatientEmail(),
                        "/queue/consultation-updates",
                        savedRequest
                    );
                    logger.debug("Sent update to patient: {}", savedRequest.getPatientEmail());
                    
                } catch (MessagingException e) {
                    logger.error("Error sending WebSocket message for chatId {}: {}", chatId, e.getMessage());
                    // Don't rethrow as the status update was successful
                }
            } else {
                logger.warn("No consultation found for chatId: {}", chatId);
            }
        } catch (Exception e) {
            logger.error("Error updating consultation status for chatId {}: {}", chatId, e.getMessage());
            throw new RuntimeException("Failed to update consultation status: " + e.getMessage());
        }
    }

    public List<ConsultationRequest> getDoctorConsultations(String doctorEmail) {
        try {
            logger.debug("Fetching all consultations for doctor: {}", doctorEmail);
            return consultationRequestRepository.findByDoctorEmail(doctorEmail);
        } catch (Exception e) {
            logger.error("Error fetching doctor consultations for {}: {}", doctorEmail, e.getMessage());
            throw new RuntimeException("Failed to fetch doctor consultations: " + e.getMessage());
        }
    }

    public List<ConsultationRequest> getPatientConsultations(String patientEmail) {
        try {
            logger.debug("Fetching all consultations for patient: {}", patientEmail);
            return consultationRequestRepository.findByPatientEmail(patientEmail);
        } catch (Exception e) {
            logger.error("Error fetching patient consultations for {}: {}", patientEmail, e.getMessage());
            throw new RuntimeException("Failed to fetch patient consultations: " + e.getMessage());
        }
    }

    public List<ConsultationRequest> getActiveConsultations(String doctorEmail) {
        try {
            logger.debug("Fetching active consultations for doctor: {}", doctorEmail);
            return consultationRequestRepository.findByDoctorEmailAndStatus(doctorEmail, "IN_PROGRESS");
        } catch (Exception e) {
            logger.error("Error fetching active consultations for {}: {}", doctorEmail, e.getMessage());
            throw new RuntimeException("Failed to fetch active consultations: " + e.getMessage());
        }
    }

    public List<ConsultationRequest> getCompletedConsultations(String doctorEmail) {
        try {
            logger.debug("Fetching completed consultations for doctor: {}", doctorEmail);
            return consultationRequestRepository.findByDoctorEmailAndStatus(doctorEmail, "COMPLETED");
        } catch (Exception e) {
            logger.error("Error fetching completed consultations for {}: {}", doctorEmail, e.getMessage());
            throw new RuntimeException("Failed to fetch completed consultations: " + e.getMessage());
        }
    }

    public List<ConsultationRequest> getPendingConsultations(String doctorEmail) {
        try {
            logger.debug("Fetching pending consultations for doctor: {}", doctorEmail);
            return consultationRequestRepository.findByDoctorEmailAndStatus(doctorEmail, "PENDING");
        } catch (Exception e) {
            logger.error("Error fetching pending consultations for {}: {}", doctorEmail, e.getMessage());
            throw new RuntimeException("Failed to fetch pending consultations: " + e.getMessage());
        }
    }
}
