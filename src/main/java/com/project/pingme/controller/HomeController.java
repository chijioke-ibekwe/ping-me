package com.project.pingme.controller;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.service.MessageService;
import com.project.pingme.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private MessageService messageService;
    private UserService userService;

    public HomeController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/chat/{userContactId}")
    @PreAuthorize("isAuthenticated()")
    public String getChat(@PathVariable Long userContactId, @ModelAttribute("newChat") ChatDTO chatform, Model model){
        model.addAttribute("messages", messageService.getMessages(userContactId));
        return "chat";
    }

    @PostMapping("/chat/{userContactId}")
    @PreAuthorize("isAuthenticated()")
    public String createChat(@PathVariable Long userContactId, @ModelAttribute("newChat") ChatDTO chatform, Authentication authentication, Model model){
        messageService.addMessage(authentication, chatform);
        chatform.setMessageText("");
        model.addAttribute("messages", messageService.getMessages(userContactId));
        return "chat";
    }

    @GetMapping("/contact")
    @PreAuthorize("isAuthenticated()")
    public String getContacts(Authentication authentication, Model model){
        model.addAttribute("contacts", userService.getContacts(authentication.getName()));
        return "home";
    }
}
