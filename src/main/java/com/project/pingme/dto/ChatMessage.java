package com.project.pingme.dto;

import java.time.LocalDateTime;

public class ChatMessage {
    private String messageId;
    private String username;
    private String messageText;
    private LocalDateTime messageTime;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getTimestamp() {
        return messageTime;
    }

    public void setTimestamp(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }
}
