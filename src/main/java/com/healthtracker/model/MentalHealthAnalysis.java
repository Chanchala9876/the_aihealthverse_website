package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "mental_health_analyses")
public class MentalHealthAnalysis {
    @Id
    private String id;
    private String userId; // Foreign key to User
    private String doctorId; // Foreign key to Doctor
    private String sessionType; // "AI_CONSULTATION", "DOCTOR_CONSULTATION"
    private String sessionStatus; // "SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    private String moodRating; // 1-10 scale
    private String stressLevel; // "LOW", "MEDIUM", "HIGH", "SEVERE"
    private String anxietyLevel; // "LOW", "MEDIUM", "HIGH", "SEVERE"
    private String depressionIndicators; // "NONE", "MILD", "MODERATE", "SEVERE"
    private List<String> symptoms; // List of reported symptoms
    private List<String> triggers; // List of reported triggers
    private String analysisNotes; // Doctor's or AI's analysis notes
    private String recommendations; // Treatment recommendations
    private String prescriptions; // Any prescribed medications
    private String followUpDate; // Next appointment date
    private String sessionDuration; // Duration in minutes
    private LocalDateTime sessionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean emergencyFlag; // Flag for urgent cases
    private String riskAssessment; // "LOW", "MEDIUM", "HIGH"

    public MentalHealthAnalysis() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MentalHealthAnalysis(String userId, String doctorId, String sessionType) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.sessionType = sessionType;
        this.sessionStatus = "SCHEDULED";
        this.emergencyFlag = false;
        this.riskAssessment = "LOW";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getSessionType() { return sessionType; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }

    public String getSessionStatus() { return sessionStatus; }
    public void setSessionStatus(String sessionStatus) { this.sessionStatus = sessionStatus; }

    public String getMoodRating() { return moodRating; }
    public void setMoodRating(String moodRating) { this.moodRating = moodRating; }

    public String getStressLevel() { return stressLevel; }
    public void setStressLevel(String stressLevel) { this.stressLevel = stressLevel; }

    public String getAnxietyLevel() { return anxietyLevel; }
    public void setAnxietyLevel(String anxietyLevel) { this.anxietyLevel = anxietyLevel; }

    public String getDepressionIndicators() { return depressionIndicators; }
    public void setDepressionIndicators(String depressionIndicators) { this.depressionIndicators = depressionIndicators; }

    public List<String> getSymptoms() { return symptoms; }
    public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }

    public List<String> getTriggers() { return triggers; }
    public void setTriggers(List<String> triggers) { this.triggers = triggers; }

    public String getAnalysisNotes() { return analysisNotes; }
    public void setAnalysisNotes(String analysisNotes) { this.analysisNotes = analysisNotes; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public String getPrescriptions() { return prescriptions; }
    public void setPrescriptions(String prescriptions) { this.prescriptions = prescriptions; }

    public String getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(String followUpDate) { this.followUpDate = followUpDate; }

    public String getSessionDuration() { return sessionDuration; }
    public void setSessionDuration(String sessionDuration) { this.sessionDuration = sessionDuration; }

    public LocalDateTime getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDateTime sessionDate) { this.sessionDate = sessionDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isEmergencyFlag() { return emergencyFlag; }
    public void setEmergencyFlag(boolean emergencyFlag) { this.emergencyFlag = emergencyFlag; }

    public String getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(String riskAssessment) { this.riskAssessment = riskAssessment; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MentalHealthAnalysis that = (MentalHealthAnalysis) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MentalHealthAnalysis{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", sessionType='" + sessionType + '\'' +
                ", sessionStatus='" + sessionStatus + '\'' +
                ", sessionDate=" + sessionDate +
                ", emergencyFlag=" + emergencyFlag +
                ", riskAssessment='" + riskAssessment + '\'' +
                '}';
    }
}
