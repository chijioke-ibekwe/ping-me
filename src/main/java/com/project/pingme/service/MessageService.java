package com.project.pingme.service;

import com.project.pingme.dto.ChatForm;
import com.project.pingme.dto.ChatMessage;
import com.project.pingme.dto.Message;
import com.project.pingme.mapper.MessageMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService {

    private MessageMapper messageMapper;

    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public List<Message> getMessages() {
       List<ChatMessage> allChatMessages = messageMapper.getAllMessages();
       List<Message> allMessages = new ArrayList<>();

       allChatMessages.forEach(chatMessage -> {
           Message message = new Message();
           message.setUsername(chatMessage.getUsername());
           message.setMessageText(chatMessage.getMessageText());
           message.setTimestamp(formatDateTime(chatMessage.getTimestamp()));

           allMessages.add(message);
       });

       return allMessages;
    }

    public void addMessage(Authentication authentication, ChatForm chatForm){
        ChatMessage message = new ChatMessage();
        message.setUsername(authentication.getName());
        message.setMessageText(chatForm.getMessageText());
        message.setTimestamp(LocalDateTime.now());
        messageMapper.insert(message);
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
