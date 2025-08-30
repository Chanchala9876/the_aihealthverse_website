package com.healthtracker.controller;

import com.healthtracker.model.ConsultationRequest;
import com.healthtracker.model.ChatMessage;
import com.healthtracker.model.Doctor;
import com.healthtracker.service.ChatService;
import com.healthtracker.service.ConsultationService;
import com.healthtracker.repository.ConsultationRequestRepository;
import com.healthtracker.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/doctor")
public class DoctorChatController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorChatController.class);
    
    @Autowired
    private ConsultationRequestRepository consultationRequestRepository;
    
    @Autowired
    private ConsultationService consultationService;
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/request-chat/{requestId}")
    public String showConsultationChat(@PathVariable String requestId, Model model, Authentication authentication) {
        try {
            Optional<ConsultationRequest> requestOpt = consultationRequestRepository.findById(requestId);
            if (requestOpt.isPresent()) {
                ConsultationRequest request = requestOpt.get();
                String doctorEmail = authentication.getName();
                Optional<Doctor> doctorOpt = doctorRepository.findByEmail(doctorEmail);
                
                if (doctorOpt.isPresent() && doctorOpt.get().getId().equals(request.getDoctorId())) {
                    Doctor doctor = doctorOpt.get();
                    
                    // Update request status to IN_PROGRESS if it's ACCEPTED
                    if ("ACCEPTED".equals(request.getStatus())) {
                        request.setStatus("IN_PROGRESS");
                        request.setRespondedAt(LocalDateTime.now());
                        consultationRequestRepository.save(request);
                        
                        // Create a system message indicating chat start
                        try {
                            ChatMessage systemMessage = new ChatMessage(
                                request.getChatId(),
                                "system",
                                "System",
                                "system",
                                request.getPatientEmail(),
                                "Chat session started with Dr. " + doctor.getName(),
                                "text"
                            );
                            chatService.saveMessage(systemMessage);
                        } catch (Exception e) {
                            logger.warn("Failed to save system message for chat start", e);
                            // Continue processing even if system message fails
                        }
                    }
                    
                    // Get chat messages
                    List<ChatMessage> messages = chatService.getChatHistory(request.getChatId());
                    
                    // Add all required attributes
                    model.addAttribute("consultationRequest", request);
                    model.addAttribute("doctor", doctor);
                    model.addAttribute("messages", messages);
                    model.addAttribute("sender", doctorEmail);
                    model.addAttribute("senderName", doctor.getName());
                    model.addAttribute("senderType", "doctor");
                    model.addAttribute("recipient", request.getPatientEmail());
                    model.addAttribute("chatId", request.getChatId());
                    model.addAttribute("title", "Chat with " + request.getPatientName());
                    model.addAttribute("status", request.getStatus());
                    
                    return "doctor/chat";
                }
            }
            return "redirect:/doctor/dashboard";
        } catch (Exception e) {
            logger.error("Error showing consultation chat: {}", e.getMessage());
            return "redirect:/doctor/dashboard";
        }
    }

    @PostMapping("/request-chat/{requestId}/send")
    @ResponseBody
    public ChatMessage sendMessage(@PathVariable String requestId, 
                                 @RequestParam String message,
                                 Authentication authentication) {
        try {
            Optional<ConsultationRequest> requestOpt = consultationRequestRepository.findById(requestId);
            if (requestOpt.isPresent()) {
                ConsultationRequest request = requestOpt.get();
                String doctorEmail = authentication.getName();
                Optional<Doctor> doctorOpt = doctorRepository.findByEmail(doctorEmail);
                
                if (doctorOpt.isPresent()) {
                    Doctor doctor = doctorOpt.get();
                    ChatMessage chatMessage = new ChatMessage(
                        request.getChatId(),
                        doctorEmail,
                        doctor.getName(),
                        "doctor",
                        request.getPatientEmail(),
                        message,
                        "text"
                    );
                    return chatService.saveMessage(chatMessage);
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("Error sending message: {}", e.getMessage());
            return null;
        }
    }
}
