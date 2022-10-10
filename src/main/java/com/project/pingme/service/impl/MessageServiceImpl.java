package com.project.pingme.service.impl;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.ChatMessageRepository;
import com.project.pingme.service.MessageService;
import com.project.pingme.service.UserContactService;
import com.project.pingme.util.Formatter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageServiceImpl implements MessageService {

    private ChatMessageRepository chatMessageRepository;
    private UserContactService userContactService;

    public MessageServiceImpl(ChatMessageRepository chatMessageRepository,
                              UserContactService userContactService) {
        this.chatMessageRepository = chatMessageRepository;
        this.userContactService = userContactService;
    }

    @Override
    public List<MessageDTO> getMessages(User authUser, Long userContactId) {
        UserContact userContact = userContactService.getContactById(userContactId);

       List<ChatMessage> allChatMessages = userContact.getChatMessages();

       List<MessageDTO> allMessages = new ArrayList<>();

       allChatMessages.forEach(chatMessage -> {
           MessageDTO message = new MessageDTO();
           message.setUserContactId(userContactId);
           message.setMessageText(chatMessage.getMessageText());
           message.setMessageTime(Formatter.formatDateTime(chatMessage.getMessageTime()));
           message.setSender(chatMessage.getSender());

           message.setUserFullName(Formatter.formatUserFullName(authUser));

           allMessages.add(message);
       });

       return allMessages;
    }

    @Transactional
    @Override
    public MessageDTO addMessage(User authUser, ChatDTO chatDTO){
        UserContact userContact = userContactService.getContactById(chatDTO.getUserContactId());

        ChatMessage message = new ChatMessage();
        message.setMessageText(chatDTO.getMessageText());
        message.setMessageTime(LocalDateTime.now());
        message.setUserContact(userContact);

        message.setSender(Formatter.formatUserFullName(authUser));

        ChatMessage savedMessage = chatMessageRepository.save(message);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageId(savedMessage.getId());
        messageDTO.setUserContactId(savedMessage.getUserContact().getId());
        messageDTO.setMessageText(savedMessage.getMessageText());
        messageDTO.setMessageTime(Formatter.formatDateTime(savedMessage.getMessageTime()));
        messageDTO.setSender(savedMessage.getSender());
        messageDTO.setUserFullName(Formatter.formatUserFullName(authUser));
        messageDTO.setRecipientId(authUser.getId().equals(userContact.getHost().getId()) ? userContact.getContact().getId():
                userContact.getHost().getId());
        messageDTO.setSenderId(authUser.getId());

        return messageDTO;
    }

}
