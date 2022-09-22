package com.project.pingme.controller;

import com.project.pingme.service.UserContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private UserContactService userContactService;

    public ContactController(UserContactService userContactService) {
        this.userContactService = userContactService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String getContacts(Authentication authentication, Model model){
        model.addAttribute("contacts", userContactService.getContacts(authentication));
        return "contact";
    }
}
