package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "consultations")
public class Consultation {
    @Id
    private String id;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String specialization;
    private String concern; // patient's main concern
    private String status; // "pending", "active", "completed", "cancelled"
    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String consultationNotes;
    private String prescription;
    private String followUpInstructions;
    private double consultationFee;
    private boolean isPaid;

    public Consultation() {
        this.requestedAt = LocalDateTime.now();
        this.status = "pending";
        this.isPaid = false;
    }

    public Consultation(String patientId, String patientName, String doctorId, 
                       String doctorName, String specialization, String concern, double consultationFee) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.concern = concern;
        this.consultationFee = consultationFee;
        this.requestedAt = LocalDateTime.now();
        this.status = "pending";
        this.isPaid = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getConcern() { return concern; }
    public void setConcern(String concern) { this.concern = concern; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getEndedAt() { return endedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }

    public String getConsultationNotes() { return consultationNotes; }
    public void setConsultationNotes(String consultationNotes) { this.consultationNotes = consultationNotes; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public String getFollowUpInstructions() { return followUpInstructions; }
    public void setFollowUpInstructions(String followUpInstructions) { this.followUpInstructions = followUpInstructions; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}
