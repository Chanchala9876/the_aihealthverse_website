package com.healthtracker.service;

import com.healthtracker.model.HealthEntry;
import com.healthtracker.model.User;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;

@Service
public class PdfExportService {

    public byte[] generateHealthReport(User user, List<HealthEntry> entries) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Add title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("HealthVerse - Health Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        // Add patient info
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        
        Paragraph patientInfo = new Paragraph("Patient Information:", headerFont);
        document.add(patientInfo);
        document.add(new Paragraph("Name: " + user.getFirstName() + " " + user.getLastName(), normalFont));
        document.add(new Paragraph("Email: " + user.getEmail(), normalFont));
        document.add(new Paragraph(" "));
        
        // Add health entries
        Paragraph entriesHeader = new Paragraph("Health Entries:", headerFont);
        document.add(entriesHeader);
        document.add(new Paragraph(" "));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (HealthEntry entry : entries) {
            Paragraph entryTitle = new Paragraph("Date: " + entry.getEntryDate().format(formatter), headerFont);
            document.add(entryTitle);
            
            document.add(new Paragraph("Mood: " + entry.getMood(), normalFont));
            document.add(new Paragraph("Sleep Hours: " + entry.getSleepHours(), normalFont));
            document.add(new Paragraph("Meals: " + String.join(", ", entry.getMeals()), normalFont));
            document.add(new Paragraph("Symptoms: " + String.join(", ", entry.getSymptoms()), normalFont));
            
            if (entry.getNotes() != null && !entry.getNotes().isEmpty()) {
                document.add(new Paragraph("Notes: " + entry.getNotes(), normalFont));
            }
            
            if (entry.getAiSuggestion() != null && !entry.getAiSuggestion().isEmpty()) {
                document.add(new Paragraph("AI Suggestion: " + entry.getAiSuggestion(), normalFont));
            }
            
            document.add(new Paragraph(" "));
        }
        
        document.close();
        return baos.toByteArray();
    }
} 