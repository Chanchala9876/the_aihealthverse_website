package com.healthtracker.service;

import com.healthtracker.model.ChatMessage;
import com.healthtracker.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistory(String chatId) {
        return chatMessageRepository.findByChatIdOrderByTimestampAsc(chatId);
    }
    
    // Method to get chat messages for consultation (using consultationId as chatId)
    public List<ChatMessage> getChatMessages(String consultationId) {
        return chatMessageRepository.findByChatIdOrderByTimestampAsc(consultationId);
    }
    
    // Method to send a message in a consultation chat
    public void sendMessage(String consultationId, String senderId, String senderName, String senderType, String messageContent) {
        ChatMessage message = new ChatMessage();
        message.setChatId(consultationId);
        message.setSender(senderId);
        message.setSenderName(senderName);
        message.setRecipient("consultation"); // For consultation chats
        message.setContent(messageContent);
        message.setSenderType(senderType); // "patient", "doctor", "ai"
        
        chatMessageRepository.save(message);
    }
}
