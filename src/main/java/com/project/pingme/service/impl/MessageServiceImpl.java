package com.project.pingme.service.impl;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.ChatMessageRepository;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.MessageService;
import com.project.pingme.service.UserService;
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
    private UserService userService;

    public MessageServiceImpl(ChatMessageRepository chatMessageRepository,
                              UserContactRepository userContactRepository,
                              UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userContactRepository = userContactRepository;
        this.userService = userService;
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

           User user = userService.getUserByUsername(authentication.getName());
           message.setUserFullName(Formatter.formatUserFullName(user));

           allMessages.add(message);
       });

       return allMessages;
    }

    @Transactional
    @Override
    public MessageDTO addMessage(Authentication authentication, ChatDTO chatDTO){
        UserContact userContact = userContactRepository.findById(chatDTO.getUserContactId()).orElseThrow(() ->
                new EntityNotFoundException("Cannot find contact"));

        ChatMessage message = new ChatMessage();
        message.setMessageText(chatDTO.getMessageText());
        message.setMessageTime(LocalDateTime.now());
        message.setUserContact(userContact);

        User user = userService.getUserByUsername(authentication.getName());
        message.setSender(Formatter.formatUserFullName(user));

        ChatMessage savedMessage = chatMessageRepository.save(message);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageId(savedMessage.getId());
        messageDTO.setUserContactId(savedMessage.getUserContact().getId());
        messageDTO.setMessageText(savedMessage.getMessageText());
        messageDTO.setMessageTime(Formatter.formatDateTime(savedMessage.getMessageTime()));
        messageDTO.setSender(savedMessage.getSender());
        messageDTO.setUserFullName(Formatter.formatUserFullName(user));
        messageDTO.setRecipientId(user.getId().equals(userContact.getHost().getId()) ? userContact.getContact().getId():
                userContact.getHost().getId());
        messageDTO.setSenderId(user.getId());

        return messageDTO;
    }

}
