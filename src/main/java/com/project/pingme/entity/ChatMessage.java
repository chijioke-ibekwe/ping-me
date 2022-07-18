package com.project.pingme.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "message_time")
    private LocalDateTime messageTime;

    @ManyToOne
    @JoinColumn(name = "user_contact_id")
    private UserContact userContact;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, LocalDateTime messageTime, UserContact userContact) {
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.userContact = userContact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(LocalDateTime messageTime) {
        this.messageTime = messageTime;
    }

    public UserContact getUserContact() {
        return userContact;
    }

    public void setUserContact(UserContact userContact) {
        this.userContact = userContact;
    }
}
