package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "consultation_requests")
public class ConsultationRequest {
    @Id
    private String id;
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String doctorId;
    private String doctorName;
    private String doctorEmail;
    private String initialMessage;
    private String status; // "PENDING", "ACCEPTED", "REJECTED", "IN_PROGRESS", "COMPLETED"
    private String chatId; // For linking to chat messages
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
    private String rejectionReason;
    private boolean emailSent;

    public ConsultationRequest() {
        this.requestedAt = LocalDateTime.now();
        this.status = "PENDING";
        this.emailSent = false;
    }

    public ConsultationRequest(String patientId, String patientName, String patientEmail,
                             String doctorId, String doctorName, String doctorEmail,
                             String initialMessage) {
        this();
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.initialMessage = initialMessage;
        this.chatId = patientEmail + "_" + doctorEmail;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorEmail() { return doctorEmail; }
    public void setDoctorEmail(String doctorEmail) { this.doctorEmail = doctorEmail; }

    public String getInitialMessage() { return initialMessage; }
    public void setInitialMessage(String initialMessage) { this.initialMessage = initialMessage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public boolean isEmailSent() { return emailSent; }
    public void setEmailSent(boolean emailSent) { this.emailSent = emailSent; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultationRequest that = (ConsultationRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ConsultationRequest{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", status='" + status + '\'' +
                ", requestedAt=" + requestedAt +
                '}';
    }
}
