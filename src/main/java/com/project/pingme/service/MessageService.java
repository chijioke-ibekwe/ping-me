package com.project.pingme.service;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.MessageDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MessageService {

    List<MessageDTO> getMessages(Authentication authentication, Long userContactId);

    MessageDTO addMessage(Authentication authentication, ChatDTO chatDTO);
}
