package com.project.pingme.controller;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.ConnectDTO;
import com.project.pingme.dto.ContactDTO;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.User;
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
public class ContactController {

    private UserContactService userContactService;
    private UserService userService;
    private SimpMessagingTemplate messagingTemplate;

    public ContactController(UserContactService userContactService, UserService userService,
                             SimpMessagingTemplate messagingTemplate) {
        this.userContactService = userContactService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/contact")
    @PreAuthorize("isAuthenticated()")
    public String getContacts(Authentication authentication, Model model){
        User user = userService.getUserByUsername(authentication.getName());
        model.addAttribute("contacts", userContactService.getContacts(authentication));
        model.addAttribute("userId", user.getId());
        model.addAttribute("username", user.getUsername());
        return "contact";
    }

    @MessageMapping("/contact/connect")
    @PreAuthorize("isAuthenticated()")
    public void requestConnection(@RequestBody ConnectDTO connectDTO, Authentication authentication){
        log.debug("Saving connection request...");
        ContactDTO contactDTO = userContactService.createContact(authentication, connectDTO);
        log.debug("Connection request saved...");

        messagingTemplate.convertAndSendToUser(
                contactDTO.getContactId().toString(),"/queue/connections", contactDTO);
    }
}
