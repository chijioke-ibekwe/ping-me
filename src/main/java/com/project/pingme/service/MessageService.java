package com.project.pingme.service;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.ChatMessageRepository;
import com.project.pingme.repository.UserContactRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService {

    private ChatMessageRepository chatMessageRepository;
    private UserContactRepository userContactRepository;

    public MessageService(ChatMessageRepository chatMessageRepository,
                          UserContactRepository userContactRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userContactRepository = userContactRepository;
    }

    public List<MessageDTO> getMessages(Long userContactId) {
        UserContact userContact = userContactRepository.findById(userContactId).orElseThrow(() ->
                new EntityNotFoundException("Cannot find contact"));

       List<ChatMessage> allChatMessages = userContact.getChatMessages();
       List<MessageDTO> allMessages = new ArrayList<>();

       allChatMessages.forEach(chatMessage -> {
           MessageDTO message = new MessageDTO();
           message.setUserContactId(userContactId);
           message.setMessageText(chatMessage.getMessageText());
           message.setMessageTime(formatDateTime(chatMessage.getMessageTime()));

           allMessages.add(message);
       });

       return allMessages;
    }

    @Transactional
    public void addMessage(Authentication authentication, ChatDTO chatDTO){
        UserContact userContact = userContactRepository.findById(chatDTO.getUserContactId()).orElseThrow(() ->
                new EntityNotFoundException("Cannot find contact"));

        ChatMessage message = new ChatMessage();
        message.setMessageText(chatDTO.getMessageText());
        message.setMessageTime(LocalDateTime.now());
        message.setUserContact(userContact);
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
