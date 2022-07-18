package com.project.pingme.service;

import com.project.pingme.dto.ChatForm;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.dto.Message;
import com.project.pingme.repository.ChatMessageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService {

    private ChatMessageRepository chatMessageRepository;

    public MessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<Message> getMessages() {
       List<ChatMessage> allChatMessages = chatMessageRepository.findAll();
       List<Message> allMessages = new ArrayList<>();

       allChatMessages.forEach(chatMessage -> {
           Message message = new Message();
           message.setMessageText(chatMessage.getMessageText());
           message.setTimestamp(formatDateTime(chatMessage.getMessageTime()));

           allMessages.add(message);
       });

       return allMessages;
    }

    @Transactional
    public void addMessage(Authentication authentication, ChatForm chatForm){
        ChatMessage message = new ChatMessage();
        message.setMessageText(chatForm.getMessageText());
        message.setMessageTime(LocalDateTime.now());
        chatMessageRepository.save(message);
    }

    private String formatDateTime(LocalDateTime localDateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
            return localDateTime.format(formatter);
        } catch (Exception e) {
            return "N/A";
        }
    }
}
