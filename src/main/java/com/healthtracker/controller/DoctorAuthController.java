package com.healthtracker.controller;

import com.healthtracker.model.Doctor;
import com.healthtracker.model.Consultation;
import com.healthtracker.repository.DoctorRepository;
import com.healthtracker.service.ChatService;
import com.healthtracker.service.DoctorService;
import com.healthtracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.healthtracker.model.ChatMessage;
import com.healthtracker.repository.ChatMessageRepository;
import com.healthtracker.model.User;
import com.healthtracker.model.ConsultationRequest;
import com.healthtracker.repository.UserRepository;
import com.healthtracker.repository.ConsultationRequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/doctor")
public class DoctorAuthController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorAuthController.class);

    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;
    private final NotificationService notificationService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final JavaMailSender mailSender;
    private final ChatService chatService;

    @Autowired
    public DoctorAuthController(
            DoctorRepository doctorRepository,
            DoctorService doctorService,
            NotificationService notificationService,
            ChatMessageRepository chatMessageRepository,
            UserRepository userRepository,
            ConsultationRequestRepository consultationRequestRepository,
            JavaMailSender mailSender,
            ChatService chatService) {
        this.doctorRepository = doctorRepository;
        this.doctorService = doctorService;
        this.notificationService = notificationService;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.consultationRequestRepository = consultationRequestRepository;
        this.mailSender = mailSender;
        this.chatService = chatService;
    }

    // Doctor Login Page
    @GetMapping("/login")
    public String showDoctorLogin(Model model) {
        model.addAttribute("title", "Doctor Login - HealthVerse");
        return "doctor/login";
    }

    // Process Doctor Login
    @PostMapping("/login")
    public String processDoctorLogin(@RequestParam String email, 
                                   @RequestParam String password,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                // For demo purposes, using simple password check
                // In production, use proper password hashing
                if (doctor.getPassword() != null && doctor.getPassword().equals(password)) {
                    session.setAttribute("doctorId", doctor.getId());
                    session.setAttribute("doctorName", doctor.getName());
                    session.setAttribute("doctorEmail", doctor.getEmail());
                    session.setAttribute("isDoctor", true);
                    
                    logger.info("Doctor login successful: {}", email);
                    return "redirect:/doctor/dashboard";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Invalid password");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Doctor not found with email: " + email);
            }
        } catch (Exception e) {
            logger.error("Error during doctor login", e);
            redirectAttributes.addFlashAttribute("error", "Login failed. Please try again.");
        }
        
        return "redirect:/doctor/login";
    }

    // Doctor Dashboard
    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(HttpSession session, Model model) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }

        try {
            Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                
                // Get consultation requests for this doctor using the new ConsultationRequest model
                System.out.println("=== DOCTOR DASHBOARD DEBUG ===");
                System.out.println("Doctor ID: " + doctorId);
                System.out.println("Doctor Name: " + doctor.getName());
                
                List<ConsultationRequest> pendingRequests = consultationRequestRepository.findByDoctorIdAndStatusOrderByRequestedAtDesc(doctorId, "PENDING");
                List<ConsultationRequest> acceptedRequests = consultationRequestRepository.findByDoctorIdAndStatusOrderByRequestedAtDesc(doctorId, "ACCEPTED");
                List<ConsultationRequest> completedRequests = consultationRequestRepository.findByDoctorIdAndStatusOrderByRequestedAtDesc(doctorId, "COMPLETED");
                
                System.out.println("Pending Requests: " + pendingRequests.size());
                System.out.println("Accepted Requests: " + acceptedRequests.size());
                System.out.println("Completed Requests: " + completedRequests.size());
                
                // Print details of pending requests
                for (ConsultationRequest req : pendingRequests) {
                    System.out.println("  - Request ID: " + req.getId() + ", Patient: " + req.getPatientName() + ", Message: " + req.getInitialMessage());
                }
                
                // Also get old consultation system data for backward compatibility
                List<Consultation> pendingConsultations = doctorService.getDoctorConsultations(doctorId)
                    .stream()
                    .filter(c -> "pending".equals(c.getStatus()))
                    .toList();
                
                List<Consultation> activeConsultations = doctorService.getDoctorConsultations(doctorId)
                    .stream()
                    .filter(c -> "active".equals(c.getStatus()))
                    .toList();
                
                List<Consultation> completedConsultations = doctorService.getDoctorConsultations(doctorId)
                    .stream()
                    .filter(c -> "completed".equals(c.getStatus()))
                    .toList();

                // Load active chat requests and filter out completed ones
                List<ConsultationRequest> activeChats = acceptedRequests.stream()
                    .filter(req -> "IN_PROGRESS".equals(req.getStatus()) || "ACCEPTED".equals(req.getStatus()))
                    .toList();
                
                // Add all model attributes
                model.addAttribute("doctor", doctor);
                model.addAttribute("pendingRequests", pendingRequests);
                model.addAttribute("acceptedRequests", acceptedRequests.stream()
                    .filter(req -> !"COMPLETED".equals(req.getStatus()))
                    .toList());
                model.addAttribute("activeChats", activeChats);
                model.addAttribute("completedRequests", completedRequests);
                model.addAttribute("pendingConsultations", pendingConsultations);
                model.addAttribute("activeConsultations", activeConsultations);
                model.addAttribute("completedConsultations", completedConsultations);
                model.addAttribute("totalConsultations", pendingConsultations.size() + activeConsultations.size() + completedConsultations.size() + pendingRequests.size() + acceptedRequests.size() + completedRequests.size());
                model.addAttribute("title", "Doctor Dashboard - " + doctor.getName());
                
                return "doctor/dashboard";
            } else {
                return "redirect:/doctor/login";
            }
        } catch (Exception e) {
            logger.error("Error loading doctor dashboard", e);
            model.addAttribute("error", "Error loading dashboard");
            return "doctor/dashboard";
        }
    }

    // Accept Consultation Request
    @PostMapping("/consultation/{consultationId}/accept")
    public String acceptConsultation(@PathVariable String consultationId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }

        try {
            Consultation consultation = doctorService.updateConsultationStatus(consultationId, "active");
            
            // Send notification to patient
            notificationService.sendConsultationAcceptedNotification(consultation);
            
            redirectAttributes.addFlashAttribute("success", "Consultation request accepted successfully!");
        } catch (Exception e) {
            logger.error("Error accepting consultation", e);
            redirectAttributes.addFlashAttribute("error", "Failed to accept consultation request");
        }

        return "redirect:/doctor/dashboard";
    }

    // Reject Consultation Request
    @PostMapping("/consultation/{consultationId}/reject")
    public String rejectConsultation(@PathVariable String consultationId,
                                   @RequestParam(required = false) String reason,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }

        try {
            Consultation consultation = doctorService.updateConsultationStatus(consultationId, "cancelled");
            
            // Send notification to patient with reason
            notificationService.sendConsultationRejectedNotification(consultation, reason);
            
            redirectAttributes.addFlashAttribute("success", "Consultation request rejected.");
        } catch (Exception e) {
            logger.error("Error rejecting consultation", e);
            redirectAttributes.addFlashAttribute("error", "Failed to reject consultation request");
        }

        return "redirect:/doctor/dashboard";
    }

    // Complete Consultation
    @PostMapping("/consultation/{consultationId}/complete")
    public String completeConsultation(@PathVariable String consultationId,
                                     @RequestParam(required = false) String notes,
                                     @RequestParam(required = false) String prescription,
                                     @RequestParam(required = false) String followUp,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }

        try {
            Optional<Consultation> consultationOpt = doctorService.getConsultationById(consultationId);
            if (consultationOpt.isPresent()) {
                Consultation consultation = consultationOpt.get();
                consultation.setStatus("completed");
                consultation.setConsultationNotes(notes);
                consultation.setPrescription(prescription);
                consultation.setFollowUpInstructions(followUp);
                
                // Save the updated consultation
                doctorService.saveConsultation(consultation);
                
                // Send completion notification to patient
                notificationService.sendConsultationCompletedNotification(consultation);
                
                redirectAttributes.addFlashAttribute("success", "Consultation completed successfully!");
            }
        } catch (Exception e) {
            logger.error("Error completing consultation", e);
            redirectAttributes.addFlashAttribute("error", "Failed to complete consultation");
        }

        return "redirect:/doctor/dashboard";
    }

    // Chat with Patient
    @GetMapping("/chat/{consultationId}")
    public String chatWithPatient(@PathVariable String consultationId,
                                HttpSession session,
                                Model model) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }

        try {
            Optional<Consultation> consultationOpt = doctorService.getConsultationById(consultationId);
            if (consultationOpt.isPresent()) {
                Consultation consultation = consultationOpt.get();
                Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
                
                if (doctorOpt.isPresent()) {
                    Doctor doctor = doctorOpt.get();
                    
                    model.addAttribute("consultation", consultation);
                    model.addAttribute("doctor", doctor);
                    model.addAttribute("chatId", consultationId);
                    model.addAttribute("sender", doctor.getEmail());
                    model.addAttribute("senderName", doctor.getName());
                    model.addAttribute("senderType", "doctor");
                    model.addAttribute("recipient", consultation.getPatientId());
                    model.addAttribute("title", "Chat with " + consultation.getPatientName());
                    
                    return "doctor/chat";
                }
            }
        } catch (Exception e) {
            logger.error("Error loading chat", e);
            model.addAttribute("error", "Failed to load chat");
        }

        return "redirect:/doctor/dashboard";
    }

    // View All Chat Requests
    @GetMapping("/chats")
    public String viewChatRequests(HttpSession session, Model model) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }
        // Find doctor by id
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "redirect:/doctor/login";
        }
        Doctor doctor = doctorOpt.get();
        // Find all chat messages where this doctor is the recipient
        List<ChatMessage> messages = chatMessageRepository.findByRecipient(doctor.getEmail());
        // Get unique chatIds
        Set<String> chatIds = messages.stream().map(ChatMessage::getChatId).collect(Collectors.toSet());
        // For each chatId, extract user email and look up user name
        List<Map<String, String>> chatRequests = new ArrayList<>();
        for (String chatId : chatIds) {
            // chatId format: userEmail_doctorEmail
            String[] parts = chatId.split("_");
            String userEmail = parts.length > 1 ? parts[0] : "unknown";
            Optional<User> userOpt = userRepository.findByEmail(userEmail);
            String userName = userOpt.map(User::getFirstName).orElse(userEmail);
            Map<String, String> chatInfo = new HashMap<>();
            chatInfo.put("chatId", chatId);
            chatInfo.put("userEmail", userEmail);
            chatInfo.put("userName", userName);
            chatRequests.add(chatInfo);
        }
        model.addAttribute("chatRequests", chatRequests);
        model.addAttribute("doctor", doctor);
        return "doctor/chat-requests";
    }

    // Doctor Logout
    @PostMapping("/logout")
    public String doctorLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/doctor/login";
    }
    
    // NEW: Accept consultation request (mental health)
    @PostMapping("/request/{requestId}/accept")
    public String acceptConsultationRequest(@PathVariable String requestId,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }
        
        try {
            Optional<ConsultationRequest> requestOpt = consultationRequestRepository.findById(requestId);
            if (requestOpt.isPresent()) {
                ConsultationRequest request = requestOpt.get();
                request.setStatus("ACCEPTED");
                request.setRespondedAt(LocalDateTime.now());
                consultationRequestRepository.save(request);
                
                // Send notification to patient about acceptance
                String patientEmail = request.getPatientEmail();
                String doctorName = request.getDoctorName();
                String chatId = request.getChatId();
                
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(patientEmail);
                mailMessage.setSubject("Your consultation request has been accepted - HealthVerse");
                mailMessage.setText(
                    "Dear " + request.getPatientName() + ",\n\n" +
                    "Dr. " + doctorName + " has accepted your consultation request.\n\n" +
                    "You can now start chatting with the doctor at:\n" +
                    "http://localhost:8090/consultation/chat/" + chatId + "\n\n" +
                    "Best regards,\nHealthVerse Team"
                );
                try {
                    mailSender.send(mailMessage);
                } catch (Exception mailError) {
                    logger.warn("Failed to send acceptance email to patient", mailError);
                    // Continue processing even if email fails
                }
                
                redirectAttributes.addFlashAttribute("success", "Consultation request accepted! You can now chat with the patient.");
                
                // Redirect to the chat interface
                return "redirect:/doctor/start-chat/" + requestId;
            } else {
                logger.warn("Consultation request not found with ID: {}", requestId);
                redirectAttributes.addFlashAttribute("error", "Consultation request not found.");
            }
        } catch (Exception e) {
            logger.error("Error accepting consultation request: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Failed to accept consultation request: " + e.getMessage());
        }
        
        return "redirect:/doctor/dashboard";
    }
    
    // NEW: Reject consultation request
    @PostMapping("/request/{requestId}/reject")
    public String rejectConsultationRequest(@PathVariable String requestId,
                                          @RequestParam(required = false) String reason,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }
        
        try {
            Optional<ConsultationRequest> requestOpt = consultationRequestRepository.findById(requestId);
            if (requestOpt.isPresent()) {
                ConsultationRequest request = requestOpt.get();
                request.setStatus("REJECTED");
                request.setRespondedAt(LocalDateTime.now());
                request.setRejectionReason(reason);
                consultationRequestRepository.save(request);
                
                redirectAttributes.addFlashAttribute("success", "Consultation request rejected.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Consultation request not found.");
            }
        } catch (Exception e) {
            logger.error("Error rejecting consultation request", e);
            redirectAttributes.addFlashAttribute("error", "Failed to reject consultation request");
        }
        
        return "redirect:/doctor/dashboard";
    }
    
    // Redirect to chat through DoctorChatController
    @GetMapping("/start-chat/{requestId}")
    public String redirectToChat(@PathVariable String requestId,
                               HttpSession session) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }
        return "redirect:/doctor/request-chat/" + requestId;
    }

    // Doctor Profile
    @GetMapping("/profile")
    public String doctorProfile(HttpSession session, Model model) {
        String doctorId = (String) session.getAttribute("doctorId");
        if (doctorId == null) {
            return "redirect:/doctor/login";
        }

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            model.addAttribute("doctor", doctorOpt.get());
            model.addAttribute("title", "Doctor Profile - HealthVerse");
            return "doctor/profile";
        }

        return "redirect:/doctor/login";
    }
}
