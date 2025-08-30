package com.healthtracker.controller;

import com.healthtracker.model.ChatMessage;
import com.healthtracker.service.ChatService;
import com.healthtracker.service.AiService;
import com.healthtracker.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private ConsultationService consultationService;

@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        try {
            logger.info("Received chat message: {} from {} to {} (chatId: {})", 
                message.getContent(), message.getSender(), message.getRecipient(), message.getChatId());
            
            // Set timestamp for the message
            message.setTimestamp(LocalDateTime.now());
            
            // Save the message
            ChatMessage savedMessage = chatService.saveMessage(message);
            
            // Handle different types of messages
            // Defensive check to prevent NullPointerException if senderType is not sent from client
            if (message.getSenderType() == null) {
                logger.warn("Received message with null senderType. Defaulting to standard message handling. ChatId: {}", message.getChatId());
                return savedMessage;
            }
            
            // Check if it's an AI interaction FIRST, regardless of senderType
            if ("ai@healthverse.com".equals(message.getRecipient())) {
                logger.info("Generating AI reply for message: {}", message.getContent());
                ChatMessage aiReply = new ChatMessage(
                    message.getChatId(),
                    "ai@healthverse.com",
                    "AI Assistant",
                    "ai",
                    message.getSender(),
                    aiService.generateChatResponse(message.getContent()),
                    "text"
                );
                aiReply.setTimestamp(LocalDateTime.now());
                ChatMessage savedAiReply = chatService.saveMessage(aiReply);
                logger.info("AI reply generated and saved: {}", savedAiReply.getContent());
                return savedAiReply;
            }
            
            switch (message.getSenderType()) {
                case "doctor":
                    // When doctor sends a message, update consultation status to IN_PROGRESS
                    consultationService.updateConsultationStatus(message.getChatId(), "IN_PROGRESS");
                    logger.info("Updated consultation status to IN_PROGRESS for chatId: {}", message.getChatId());
                    return savedMessage;
                    
                case "patient":
                    // For patient messages, just return the saved message
                    return savedMessage;
                    
                case "system":
                    // System messages are just broadcasted as is
                    return savedMessage;
                    
                default:
                    return savedMessage;
            }
        } catch (Exception e) {
            logger.error("Error processing chat message: {}", e.getMessage(), e);
            // Create and return an error message
            ChatMessage errorMessage = new ChatMessage(
                message.getChatId(),
                "system",
                "System",
                "system",
                message.getSender(),
                "Error processing message. Please try again.",
                "error"
            );
            errorMessage.setTimestamp(LocalDateTime.now());
            return errorMessage;
        }
    }

    @GetMapping("/api/chat/history/{chatId}")
    @ResponseBody
    public ResponseEntity<?> getChatHistory(@PathVariable String chatId) {
        try {
            List<ChatMessage> messages = chatService.getChatHistory(chatId);
            logger.info("Retrieved {} messages for chatId: {}", messages.size(), chatId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Error retrieving chat history for chatId {}: {}", chatId, e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error retrieving chat history: " + e.getMessage());
        }
    }
} 