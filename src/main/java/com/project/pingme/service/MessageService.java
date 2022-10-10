package com.project.pingme.service;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.User;

import java.util.List;

public interface MessageService {

    List<MessageDTO> getMessages(User authUser, Long userContactId);

    MessageDTO addMessage(User authUser, ChatDTO chatDTO);
}
