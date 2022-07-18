package com.project.pingme.service;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.dto.MessageDTO;
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

    public List<MessageDTO> getMessages() {
       List<ChatMessage> allChatMessages = chatMessageRepository.findAll();
       List<MessageDTO> allMessages = new ArrayList<>();

       allChatMessages.forEach(chatMessage -> {
           MessageDTO message = new MessageDTO();
           message.setMessageText(chatMessage.getMessageText());
           message.setTimestamp(formatDateTime(chatMessage.getMessageTime()));

           allMessages.add(message);
       });

       return allMessages;
    }

    @Transactional
    public void addMessage(Authentication authentication, ChatDTO chatDTO){
        ChatMessage message = new ChatMessage();
        message.setMessageText(chatDTO.getMessageText());
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
