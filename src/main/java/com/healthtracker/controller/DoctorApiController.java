package com.healthtracker.controller;

import com.healthtracker.model.ConsultationStats;
import com.healthtracker.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
public class DoctorApiController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping("/consultation-stats")
    public ResponseEntity<ConsultationStats> getConsultationStats(Authentication authentication) {
        String doctorEmail = authentication.getName();
        
        int pendingCount = consultationService.getPendingConsultations(doctorEmail).size();
        int activeCount = consultationService.getActiveConsultations(doctorEmail).size();
        int completedCount = consultationService.getCompletedConsultations(doctorEmail).size();
        int totalCount = pendingCount + activeCount + completedCount;

        ConsultationStats stats = new ConsultationStats(
            pendingCount,
            activeCount,
            completedCount,
            totalCount
        );

        return ResponseEntity.ok(stats);
    }
}
