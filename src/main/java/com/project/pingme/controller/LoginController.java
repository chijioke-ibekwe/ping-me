package com.project.pingme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping
    public String getLandingPage(){
        return "landing";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
}
