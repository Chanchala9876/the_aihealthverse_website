package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String specialization; // mental-health, skin, bones, eyes, cardiology, general
    private String qualifications;
    private int experienceYears;
    private String profileImage;
    private String bio;
    private double rating;
    private int totalConsultations;
    private boolean available = true;
    private List<String> availableTimeSlots;
    private String consultationFee;
    private String language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Doctor() {}

    public Doctor(String name, String email, String password, String specialization, boolean available) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.specialization = specialization;
        this.available = available;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Enhanced constructor
    public Doctor(String name, String email, String specialization, String qualifications, 
                  int experienceYears, String bio, double rating, String consultationFee, String language) {
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.qualifications = qualifications;
        this.experienceYears = experienceYears;
        this.bio = bio;
        this.rating = rating;
        this.consultationFee = consultationFee;
        this.language = language;
        this.available = true;
        this.totalConsultations = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public int getTotalConsultations() { return totalConsultations; }
    public void setTotalConsultations(int totalConsultations) { this.totalConsultations = totalConsultations; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public List<String> getAvailableTimeSlots() { return availableTimeSlots; }
    public void setAvailableTimeSlots(List<String> availableTimeSlots) { this.availableTimeSlots = availableTimeSlots; }
    
    public String getConsultationFee() { return consultationFee; }
    public void setConsultationFee(String consultationFee) { this.consultationFee = consultationFee; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", qualifications='" + qualifications + '\'' +
                ", experienceYears=" + experienceYears +
                ", rating=" + rating +
                ", totalConsultations=" + totalConsultations +
                ", available=" + available +
                '}';
    }
}
