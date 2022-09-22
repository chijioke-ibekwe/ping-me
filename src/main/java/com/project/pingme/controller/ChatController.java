package com.project.pingme.controller;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.service.MessageService;
import com.project.pingme.service.UserContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private MessageService messageService;
    private UserContactService userContactService;

    public ChatController(MessageService messageService, UserContactService userContactService) {
        this.messageService = messageService;
        this.userContactService = userContactService;
    }

    @GetMapping("/{userContactId}")
    @PreAuthorize("isAuthenticated()")
    public String getChat(@PathVariable Long userContactId, @ModelAttribute("newChat") ChatDTO chatform, Authentication authentication, Model model){
        model.addAttribute("messages", messageService.getMessages(authentication, userContactId));
        return "chat";
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public String createChat(@ModelAttribute("newChat") ChatDTO chatform, Authentication authentication, Model model){
        messageService.addMessage(authentication, chatform);
        chatform.setMessageText("");
        model.addAttribute("messages", messageService.getMessages(authentication, chatform.getUserContactId()));
        return "chat";
    }

    @GetMapping("/contact")
    @PreAuthorize("isAuthenticated()")
    public String getContacts(Authentication authentication, Model model){
        model.addAttribute("contacts", userContactService.getContacts(authentication));
        return "contact";
    }
}
