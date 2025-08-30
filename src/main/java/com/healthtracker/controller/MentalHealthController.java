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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.UnsupportedEncodingException;
import org.springframework.web.util.UriUtils;

@Controller
@RequestMapping("/mental-health")
public class MentalHealthController {
    private static final Logger logger = LoggerFactory.getLogger(MentalHealthController.class);
    
    private final DoctorRepository doctorRepository;
    private final MentalHealthAnalysisRepository mentalHealthAnalysisRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final ChatService chatService;
    
    @Autowired
    public MentalHealthController(
            DoctorRepository doctorRepository,
            MentalHealthAnalysisRepository mentalHealthAnalysisRepository,
            UserRepository userRepository,
            JavaMailSender mailSender,
            ConsultationRequestRepository consultationRequestRepository,
            ChatService chatService) {
        this.doctorRepository = doctorRepository;
        this.mentalHealthAnalysisRepository = mentalHealthAnalysisRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.consultationRequestRepository = consultationRequestRepository;
        this.chatService = chatService;
    }

    @GetMapping
    public String mentalHealthLanding(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "mental-health";
    }

    @GetMapping("/chat/{doctorId}")
    public String chatWithDoctor(@PathVariable String doctorId, 
                               @RequestParam(required = false) String sessionId,
                               Model model) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                return "redirect:/mental-health?error=doctor-not-found";
            }
            
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(userEmail).orElse(null);
            if (user == null) {
                return "redirect:/mental-health?error=user-not-found";
            }
            
            String chatId = userEmail + "_" + doctor.getEmail();
            
            // Get existing consultation request if any
            List<ConsultationRequest> activeRequests = consultationRequestRepository
                .findByPatientEmailAndDoctorIdAndStatus(userEmail, doctorId, "IN_PROGRESS");
            
            ConsultationRequest activeRequest = activeRequests.isEmpty() ? null : activeRequests.get(0);
            
            model.addAttribute("chatId", chatId);
            model.addAttribute("sender", userEmail);
            model.addAttribute("senderName", user.getFirstName() + " " + user.getLastName());
            model.addAttribute("recipient", doctor.getEmail());
            model.addAttribute("doctorName", doctor.getName());
            model.addAttribute("consultationRequest", activeRequest);
            model.addAttribute("sessionId", sessionId);
            
            // Get chat history
            List<ChatMessage> chatHistory = chatService.getChatMessages(chatId);
            model.addAttribute("chatHistory", chatHistory);
            
            return "chat";
        } catch (Exception e) {
            logger.error("Error in chatWithDoctor: {}", e.getMessage(), e);
            return "redirect:/mental-health?error=chat-error";
        }
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
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Unauthenticated user tried to contact doctorId: {}", doctorId);
                return "redirect:/login?error=unauthenticated";
            }
            String userEmail = authentication.getName();
            logger.info("Contact request for doctorId: {} from user: {}", doctorId, userEmail);
            
            User user = userRepository.findByEmail(userEmail).orElse(null);
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            
            logger.debug("User found: {}, Doctor found: {}", (user != null), (doctor != null));
            
            if (user == null || doctor == null) {
                logger.error("User or Doctor not found. User: {}, Doctor: {}", userEmail, doctorId);
                return "redirect:/mental-health?error=user-or-doctor-not-found";
            }
            
            // Create consultation request with proper initialization
            String chatId = user.getEmail() + "_" + doctor.getEmail();
            ConsultationRequest request = new ConsultationRequest(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                message
            );
            request.setStatus("PENDING");
            request.setRequestedAt(LocalDateTime.now());
            request.setChatId(chatId);
            
            logger.info("Creating ConsultationRequest:");
            logger.info("Patient ID: {}", user.getId());
            logger.info("Patient Name: {}", user.getFirstName() + " " + user.getLastName());
            logger.info("Doctor ID: {}", doctor.getId());
            logger.info("Chat ID: {}", chatId);
            logger.info("Status: {}", request.getStatus());
            
            // Save the consultation request
            ConsultationRequest savedRequest = consultationRequestRepository.save(request);
            logger.info("ConsultationRequest saved with ID: {}", savedRequest.getId());
            
            // Create the initial chat message
            try {
                // Create system notification message
                ChatMessage systemMessage = new ChatMessage(
                    request.getChatId(),
                    "system",
                    "System",
                    "system",
                    doctor.getEmail(),
                    "New consultation request from " + user.getFirstName() + " " + user.getLastName(),
                    "notification"
                );
                chatService.saveMessage(systemMessage);
                
                // Create the actual patient message
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
                
                logger.info("Initial chat messages created for consultation {}", request.getChatId());
            } catch (Exception e) {
                logger.error("Failed to save chat messages: {}", e.getMessage(), e);
                // Continue processing even if chat message creation fails
            }
            
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
                logger.error("Failed to send email notification for consultation request {}: {}", savedRequest.getId(), e.getMessage());
            }
            
            // Finalize the consultation request and redirect to chat
            if (savedRequest != null && savedRequest.getChatId() != null) {
                // The model attributes won't persist through a redirect, 
                // so we'll add necessary information as query parameters
                return "redirect:/consultation/chat/" + savedRequest.getChatId() 
                    + "?status=pending"
                    + "&requestId=" + savedRequest.getId()
                    + "&doctorName=" + java.net.URLEncoder.encode(doctor.getName(), "UTF-8");
            } else {
                logger.error("Failed to create consultation request properly");
                return "redirect:/mental-health?error=request-creation-failed";
            }
            
        } catch (Exception e) {
            logger.error("Error in contactDoctor: {}", e.getMessage(), e);
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
