package com.healthtracker.controller;

import com.healthtracker.model.HealthEntry;
import com.healthtracker.model.User;
import com.healthtracker.service.HealthService;
import com.healthtracker.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private HealthService healthService;

    @Autowired
    private PdfExportService pdfExportService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportHealthReport() {
        try {
            // For demo purposes, create a mock user
            User user = new User();
            user.setFirstName("Demo");
            user.setLastName("User");
            user.setEmail("demo@healthverse.com");
            
            String userId = "demo-user";
            List<HealthEntry> entries = healthService.getUserEntries(userId);
            
            byte[] pdfBytes = pdfExportService.generateHealthReport(user, entries);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "health-report.pdf");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 