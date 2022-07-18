package com.project.pingme.controller;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.service.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatController {

    private MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    public String getChat(@ModelAttribute("newChat") ChatDTO chatform, Model model){
        model.addAttribute("messages", messageService.getMessages());
        return "chat";
    }

    @PostMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    public String createChat(@ModelAttribute("newChat") ChatDTO chatform, Authentication authentication, Model model){
        messageService.addMessage(authentication, chatform);
        chatform.setMessageText("");
        model.addAttribute("messages", messageService.getMessages());
        return "chat";
    }
}
