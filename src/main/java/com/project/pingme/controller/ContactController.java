package com.project.pingme.controller;

import com.project.pingme.entity.User;
import com.project.pingme.service.UserContactService;
import com.project.pingme.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

    public ContactController(UserContactService userContactService, UserService userService) {
        this.userContactService = userContactService;
        this.userService = userService;
    }

    @GetMapping("/contact")
    @PreAuthorize("isAuthenticated()")
    public String getContacts(Authentication authentication, Model model){
        User authUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("contacts", userContactService.getContactDTOS(authUser));
        model.addAttribute("user", authUser);
        return "contact/contact";
    }
}
