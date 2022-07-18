package com.project.pingme.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_contacts")
public class UserContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private User contact;

    @OneToMany(mappedBy = "userContact")
    private List<ChatMessage> chatMessages;

    public UserContact() {
    }

    public UserContact(User host, User contact, List<ChatMessage> chatMessages) {
        this.host = host;
        this.contact = contact;
        this.chatMessages = chatMessages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getContact() {
        return contact;
    }

    public void setContact(User contact) {
        this.contact = contact;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
