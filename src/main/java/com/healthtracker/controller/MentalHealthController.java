package com.healthtracker.controller;

import com.healthtracker.model.Doctor;
import com.healthtracker.model.MentalHealthAnalysis;
import com.healthtracker.model.User;
import com.healthtracker.model.ConsultationRequest;
import com.healthtracker.model.ChatMessage;
import com.healthtracker.repository.DoctorRepository;
import com.healthtracker.repository.MentalHealthAnalysisRepository;
import com.healthtracker.repository.UserRepository;
import com.healthtracker.repository.ConsultationRequestRepository;
import com.healthtracker.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/mental-health")
public class MentalHealthController {
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private MentalHealthAnalysisRepository mentalHealthAnalysisRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ConsultationRequestRepository consultationRequestRepository;
    
    @Autowired
    private ChatService chatService;

    @GetMapping
    public String mentalHealthLanding(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "mental-health";
    }

    @GetMapping("/chat/{doctorId}")
    public String chatWithDoctor(@PathVariable String doctorId, Model model) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor != null) {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            String chatId = userEmail + "_" + doctor.getEmail();
            model.addAttribute("chatId", chatId);
            model.addAttribute("sender", userEmail);
            model.addAttribute("recipient", doctor.getEmail());
            model.addAttribute("doctorName", doctor.getName());
            return "chat";
        }
        return "redirect:/mental-health?error=doctor-not-found";
    }

    @GetMapping("/ai-chat")
    public String chatWithAI(Model model) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String chatId = userEmail + "_ai";
        model.addAttribute("chatId", chatId);
        model.addAttribute("sender", userEmail);
        model.addAttribute("recipient", "ai@healthverse.com");
        model.addAttribute("doctorName", "HealthVerse AI");
        return "chat";
    }

    @PostMapping("/contact/{doctorId}")
    public String contactDoctor(@PathVariable String doctorId, @RequestParam String message, Authentication authentication, Model model) {
        try {
            String userEmail = authentication.getName();
            System.out.println("=== CONTACT DOCTOR DEBUG ===");
            System.out.println("Doctor ID: " + doctorId);
            System.out.println("User Email: " + userEmail);
            System.out.println("Message: " + message);
            
            User user = userRepository.findByEmail(userEmail).orElse(null);
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            
            System.out.println("User found: " + (user != null ? user.getFirstName() + " " + user.getLastName() : "NULL"));
            System.out.println("Doctor found: " + (doctor != null ? doctor.getName() : "NULL"));
            
            if (user == null || doctor == null) {
                System.out.println("ERROR: User or Doctor is null!");
                return "redirect:/mental-health?error=user-or-doctor-not-found";
            }
            
            // Create consultation request
            ConsultationRequest request = new ConsultationRequest(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                message
            );
            
            System.out.println("Creating ConsultationRequest:");
            System.out.println("Patient ID: " + user.getId());
            System.out.println("Patient Name: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("Doctor ID: " + doctor.getId());
            System.out.println("Chat ID: " + request.getChatId());
            System.out.println("Status: " + request.getStatus());
            
            // Save the consultation request
            ConsultationRequest savedRequest = consultationRequestRepository.save(request);
            System.out.println("ConsultationRequest saved with ID: " + savedRequest.getId());
            
            // Create the initial chat message
            ChatMessage initialMessage = new ChatMessage(
                request.getChatId(),
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                "patient",
                doctor.getEmail(),
                message,
                "text"
            );
            chatService.saveMessage(initialMessage);
            
            // Send email notification to doctor
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(doctor.getEmail());
                mailMessage.setSubject("🩺 New Mental Health Consultation Request - HealthVerse");
                mailMessage.setText(
                    "Dear Dr. " + doctor.getName() + ",\n\n" +
                    "You have received a new mental health consultation request from a patient.\n\n" +
                    "📋 PATIENT DETAILS:\n" +
                    "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "Request Time: " + LocalDateTime.now() + "\n\n" +
                    "📝 INITIAL MESSAGE:\n" +
                    "\"" + message + "\"\n\n" +
                    "🔗 TO RESPOND:\n" +
                    "1. Login to your doctor portal: http://localhost:8090/doctor/login\n" +
                    "2. Check your pending consultation requests\n" +
                    "3. Accept the request to start real-time chat\n\n" +
                    "Thank you for providing mental health support.\n\n" +
                    "Best regards,\n" +
                    "HealthVerse Team"
                );
                mailSender.send(mailMessage);
                request.setEmailSent(true);
                consultationRequestRepository.save(request);
            } catch (Exception e) {
                System.err.println("Failed to send email notification: " + e.getMessage());
            }
            
            // Redirect user to chat page
            return "redirect:/mental-health/chat/" + doctorId + "?success=request-sent";
            
        } catch (Exception e) {
            System.err.println("Error in contactDoctor: " + e.getMessage());
            return "redirect:/mental-health?error=request-failed";
        }
    }
    
    // Fix for the redirect issue - AI advice endpoint
    @GetMapping("/ai-advice")
    public String getAiMentalHealthAdvice(Model model, Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        
        if (user != null) {
            // Create a new AI mental health analysis session
            MentalHealthAnalysis analysis = new MentalHealthAnalysis(user.getId(), "ai", "AI_CONSULTATION");
            analysis.setSessionDate(LocalDateTime.now());
            analysis.setSessionStatus("IN_PROGRESS");
            mentalHealthAnalysisRepository.save(analysis);
            
            // Get user's previous mental health analyses for context
            List<MentalHealthAnalysis> previousAnalyses = mentalHealthAnalysisRepository.findByUserIdOrderBySessionDateDesc(user.getId());
            
            model.addAttribute("currentAnalysis", analysis);
            model.addAttribute("previousAnalyses", previousAnalyses);
            model.addAttribute("chatId", userEmail + "_ai");
            model.addAttribute("sender", userEmail);
            model.addAttribute("recipient", "ai@healthverse.com");
            model.addAttribute("doctorName", "HealthVerse AI Assistant");
            
            return "chat"; // Use the existing chat template
        }
        
        return "redirect:/mental-health?error=user-not-found";
    }
    
    // Fix for the redirect issue - Find doctors endpoint
    @GetMapping("/find-doctors")
    public String findMentalHealthDoctors(Model model) {
        // Find doctors specializing in mental health
        List<Doctor> mentalHealthDoctors = doctorRepository.findAll().stream()
            .filter(doctor -> doctor.getSpecialization() != null && 
                   doctor.getSpecialization().toLowerCase().contains("mental"))
            .toList();
        
        if (mentalHealthDoctors.isEmpty()) {
            // If no specific mental health doctors, show all available doctors
            mentalHealthDoctors = doctorRepository.findAll();
        }
        
        model.addAttribute("doctors", mentalHealthDoctors);
        model.addAttribute("pageTitle", "Find Mental Health Doctors");
        
        return "mental-health"; // Reuse the existing template
    }
    
    // Get user's mental health history
    @GetMapping("/history")
    public String getMentalHealthHistory(Model model, Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        
        if (user != null) {
            List<MentalHealthAnalysis> analyses = mentalHealthAnalysisRepository.findByUserIdOrderBySessionDateDesc(user.getId());
            model.addAttribute("analyses", analyses);
            model.addAttribute("user", user);
            
            return "mental-health-history"; // Create this template later if needed
        }
        
        return "redirect:/mental-health?error=user-not-found";
    }
    
    // Start a new mental health session with a specific doctor
    @PostMapping("/start-session/{doctorId}")
    public String startMentalHealthSession(@PathVariable String doctorId, Authentication authentication, Model model) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        
        if (user != null && doctor != null) {
            // Create a new mental health analysis session
            MentalHealthAnalysis analysis = new MentalHealthAnalysis(user.getId(), doctorId, "DOCTOR_CONSULTATION");
            analysis.setSessionDate(LocalDateTime.now());
            analysis.setSessionStatus("SCHEDULED");
            mentalHealthAnalysisRepository.save(analysis);
            
            return "redirect:/mental-health/chat/" + doctorId + "?sessionId=" + analysis.getId();
        }
        
        return "redirect:/mental-health?error=session-creation-failed";
    }
}
