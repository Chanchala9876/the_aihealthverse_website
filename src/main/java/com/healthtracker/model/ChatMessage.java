package com.healthtracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;
    private String chatId; // consultation session ID
    private String sender; // userId or doctorId
    private String senderName; // display name
    private String senderType; // "patient" or "doctor"
    private String recipient; // userId or doctorId
    private String content;
    private String messageType; // "text", "image", "file", "prescription"
    private boolean isRead;
    private LocalDateTime timestamp;
    private String attachmentUrl; // for images/files

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    public ChatMessage(String chatId, String sender, String senderName, String senderType, 
                      String recipient, String content, String messageType) {
        this.chatId = chatId;
        this.sender = sender;
        this.senderName = senderName;
        this.senderType = senderType;
        this.recipient = recipient;
        this.content = content;
        this.messageType = messageType != null ? messageType : "text";
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getChatId() { return chatId; }
    public void setChatId(String chatId) { this.chatId = chatId; }
    
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    
    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
}
