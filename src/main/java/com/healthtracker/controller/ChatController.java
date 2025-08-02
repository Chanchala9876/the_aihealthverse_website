package com.healthtracker.controller;

import com.healthtracker.model.ChatMessage;
import com.healthtracker.service.ChatService;
import com.healthtracker.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
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

@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        logger.info("Received chat message: {} from {} to {} (chatId: {})", message.getContent(), message.getSender(), message.getRecipient(), message.getChatId());
        message.setTimestamp(LocalDateTime.now());
        chatService.saveMessage(message);
        
        // If the recipient is AI, generate an AI reply and broadcast it
        if ("ai@healthverse.com".equals(message.getRecipient())) {
            logger.info("Generating AI reply for message: {}", message.getContent());
            ChatMessage aiReply = new ChatMessage();
            aiReply.setChatId(message.getChatId());
            aiReply.setSender("ai@healthverse.com");
            aiReply.setSenderName("AI Assistant");
            aiReply.setSenderType("ai");
            aiReply.setRecipient(message.getSender());
            aiReply.setTimestamp(LocalDateTime.now());
            aiReply.setContent(aiService.generateMoodAdvice(message.getContent(), ""));
            chatService.saveMessage(aiReply);
            logger.info("AI reply generated: {}", aiReply.getContent());
            return aiReply;
        }
        
        // For doctor-patient communication, just return the message
        logger.info("Broadcasting doctor-patient message to topic");
        return message;
    }

    @GetMapping("/api/chat/history/{chatId}")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@PathVariable String chatId) {
        return chatService.getChatHistory(chatId);
    }
} 