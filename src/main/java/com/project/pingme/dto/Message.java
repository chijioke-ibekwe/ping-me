package com.project.pingme.dto;

public class Message {
    private String username;
    private String messageText;
    private String messageTime;

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

    public String getTimestamp() {
        return messageTime;
    }

    public void setTimestamp(String messageTime) {
        this.messageTime = messageTime;
    }
}
