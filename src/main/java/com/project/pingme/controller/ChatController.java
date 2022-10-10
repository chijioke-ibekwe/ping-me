package com.project.pingme.controller;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.ChatNotificationDTO;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.User;
import com.project.pingme.service.MessageService;
import com.project.pingme.service.UserContactService;
import com.project.pingme.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class ChatController {

    private MessageService messageService;
    private UserContactService userContactService;
    private SimpMessagingTemplate messagingTemplate;
    private UserService userService;

    public ChatController(MessageService messageService, UserContactService userContactService,
                          SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messageService = messageService;
        this.userContactService = userContactService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @GetMapping("/chat/{userContactId}")
    @PreAuthorize("isAuthenticated()")
    public String getChat(@PathVariable Long userContactId, @ModelAttribute("newChat") ChatDTO chatform, Authentication authentication, Model model){
        User authUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("messages", messageService.getMessages(authUser, userContactId));
        model.addAttribute("userId", authUser.getId());
        model.addAttribute("username", authUser.getUsername());
        model.addAttribute("userContactId", userContactId);
        return "chat";
    }

    @MessageMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    public void createChat(@RequestBody ChatDTO chatform, Authentication authentication){
        User authUser = userService.getUserByUsername(authentication.getName());
        log.debug("Adding message starting ...");
        MessageDTO messageDTO = messageService.addMessage(authUser, chatform);
        log.debug("Adding message completed ...");

        messagingTemplate.convertAndSendToUser(
                messageDTO.getRecipientId().toString(),"/queue/messages", messageDTO);
    }
}
