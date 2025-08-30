package com.healthtracker.controller;

import com.healthtracker.model.Doctor;
import com.healthtracker.model.Consultation;
import com.healthtracker.model.ChatMessage;
import com.healthtracker.service.DoctorService;
import com.healthtracker.service.AiService;
import com.healthtracker.service.ChatService;
import com.healthtracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller
public class ConsultationController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);

    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AiService aiService;

    @Autowired
    private ChatService chatService;

    // Redirect from /consultation/dashboard to /health/entries as requested
    @GetMapping("/consultation/dashboard")
    public String redirectToHealthEntries() {
        return "redirect:/health/entries";
    }

    // General AI Health Advice (moved from mental health specific route to avoid conflicts)
    @GetMapping("/consultation/ai-advice")
    public String showGeneralHealthAdvice(Model model) {
        model.addAttribute("title", "AI Health Advisor - HealthVerse");
        return "consultation/ai-advice";
    }

    @PostMapping("/consultation/ai-advice")
    public String getGeneralHealthAdvice(@RequestParam String concern, 
                                       @RequestParam String mood, 
                                       @RequestParam String symptoms,
                                       Model model) {
        try {
            // Generate comprehensive health advice
            String advice = aiService.generateMentalHealthAdvice(concern, mood, symptoms);
            
            model.addAttribute("advice", advice);
            model.addAttribute("concern", concern);
            model.addAttribute("mood", mood);
            model.addAttribute("symptoms", symptoms);
            model.addAttribute("title", "AI Health Advice - HealthVerse");
            
            return "consultation/health-result";
        } catch (Exception e) {
            logger.error("Error generating health advice", e);
            model.addAttribute("errorMessage", "Sorry, something went wrong: " + e.getMessage());
            return "error";
        }
    }

    // Doctor Selection
    @GetMapping("/consultation/doctors")
    public String showDoctors(@RequestParam(required = false) String specialization, Model model) {
        // Initialize sample doctors if none exist
        doctorService.initializeSampleDoctors();
        
        List<Doctor> doctors;
        if (specialization != null && !specialization.isEmpty()) {
            doctors = doctorService.getDoctorsBySpecialization(specialization);
        } else {
            doctors = doctorService.getAvailableDoctors();
        }
        
        model.addAttribute("doctors", doctors);
        model.addAttribute("selectedSpecialization", specialization);
        model.addAttribute("title", "Find Doctors - HealthVerse");
        
        return "consultation/doctors";
    }

    // Doctor Profile
    @GetMapping("/consultation/doctor/{doctorId}")
    public String showDoctorProfile(@PathVariable String doctorId, Model model) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Doctor not found");
            return "error";
        }

        model.addAttribute("doctor", doctorOpt.get());
        model.addAttribute("title", "Doctor Profile - HealthVerse");
        
        return "consultation/doctor-profile";
    }

    // Request Consultation
    @PostMapping("/consultation/request")
    public String requestConsultation(@RequestParam String doctorId,
                                    @RequestParam String concern,
                                    @RequestParam String specialization,
                                    Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String patientId = authentication.getName();
            String patientName = authentication.getName(); // In real app, get from user profile

            Consultation consultation = doctorService.requestConsultation(
                patientId, patientName, doctorId, concern, specialization
            );
            
            // Send email notification to doctor
            notificationService.sendConsultationRequestNotification(consultation);

            model.addAttribute("consultation", consultation);
            model.addAttribute("title", "Consultation Requested - HealthVerse");
            
            return "consultation/request-success";
        } catch (Exception e) {
            logger.error("Error requesting consultation", e);
            model.addAttribute("errorMessage", "Failed to request consultation: " + e.getMessage());
            return "error";
        }
    }

    // User's Consultations
    @GetMapping("/consultation/my-consultations")
    public String showMyConsultations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String patientId = authentication.getName();
        
        List<Consultation> consultations = doctorService.getPatientConsultations(patientId);
        
        model.addAttribute("consultations", consultations);
        model.addAttribute("title", "My Consultations - HealthVerse");
        
        return "consultation/my-consultations";
    }

    // Chat Interface
    @GetMapping("/consultation/chat/{chatId}")
    public String showChat(@PathVariable String chatId, @RequestParam(required = false) String status, Model model) {
        try {
            // Get current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            
            // Get chat messages
            List<ChatMessage> messages = chatService.getChatMessages(chatId);
            
            // Add chat details to model
            model.addAttribute("chatId", chatId);
            model.addAttribute("messages", messages);
            model.addAttribute("currentUserEmail", currentUserEmail);
            
            if ("pending".equals(status)) {
                model.addAttribute("status", "pending");
                model.addAttribute("statusMessage", "Your consultation request has been sent. Please wait for the doctor to accept.");
            }
            
            return "consultation/chat";
        } catch (Exception e) {
            logger.error("Error loading chat", e);
            model.addAttribute("errorMessage", "Failed to load chat: " + e.getMessage());
            return "error";
        }
    }

    // Send Chat Message (AJAX endpoint)
    @PostMapping("/consultation/chat/{consultationId}/send")
    @ResponseBody
    public String sendMessage(@PathVariable String consultationId,
                            @RequestParam String message) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String senderId = authentication.getName();
            String senderName = authentication.getName(); // In real app, get from user profile
            
            chatService.sendMessage(consultationId, senderId, senderName, "patient", message);
            return "success";
        } catch (Exception e) {
            logger.error("Error sending message", e);
            return "error";
        }
    }
}
