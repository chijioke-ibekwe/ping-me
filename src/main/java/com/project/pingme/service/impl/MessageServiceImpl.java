package com.project.pingme.service.impl;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.ChatMessageRepository;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.service.MessageService;
import com.project.pingme.util.Formatter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageServiceImpl implements MessageService {

    private ChatMessageRepository chatMessageRepository;
    private UserContactRepository userContactRepository;

    public MessageServiceImpl(ChatMessageRepository chatMessageRepository,
                              UserContactRepository userContactRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userContactRepository = userContactRepository;
    }

    @Override
    public List<MessageDTO> getMessages(Authentication authentication, Long userContactId) {
        UserContact userContact = userContactRepository.findById(userContactId).orElseThrow(() ->
                new EntityNotFoundException("Cannot find contact"));

       List<ChatMessage> allChatMessages = userContact.getChatMessages();
       List<MessageDTO> allMessages = new ArrayList<>();

       allChatMessages.forEach(chatMessage -> {
           MessageDTO message = new MessageDTO();
           message.setUserContactId(userContactId);
           message.setMessageText(chatMessage.getMessageText());
           message.setMessageTime(Formatter.formatDateTime(chatMessage.getMessageTime()));
           message.setSender(chatMessage.getSender());
           message.setUsername(authentication.getName());

           allMessages.add(message);
       });

       return allMessages;
    }

    @Transactional
    @Override
    public void addMessage(Authentication authentication, ChatDTO chatDTO){
        UserContact userContact = userContactRepository.findById(chatDTO.getUserContactId()).orElseThrow(() ->
                new EntityNotFoundException("Cannot find contact"));

        ChatMessage message = new ChatMessage();
        message.setMessageText(chatDTO.getMessageText());
        message.setMessageTime(LocalDateTime.now());
        message.setUserContact(userContact);
        message.setSender(authentication.getName());

        chatMessageRepository.save(message);
    }

}
