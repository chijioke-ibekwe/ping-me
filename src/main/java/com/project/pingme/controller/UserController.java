package com.project.pingme.controller;

import com.project.pingme.dto.SignupDTO;
import com.project.pingme.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/logout")
    public String logoutUser(){
        return "login";
    }

    @GetMapping("/signup")
    public String getSignUpPage(@ModelAttribute("signupDTO") SignupDTO signupDTO, Model model){
        return "signup";
    }

    @PostMapping("/signup")
    public String createAccount(@ModelAttribute("signupDTO") SignupDTO signupDTO, Model model){

        String signupError = null;

        if(userService.isAvailable(signupDTO.getUsername())){
            signupError = "Username already exists";
        }

        if(!signupDTO.getPassword().equals(signupDTO.getConfirmPassword())){
            signupError = "Password fields have to match";
        }

        if (signupError == null) {
            Long rowsAdded = userService.createUser(signupDTO);
            if (rowsAdded < 1) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupError", null);
            model.addAttribute("signupSuccess", true);
            model.addAttribute("successMessage", "Sign Up Successful!");
        } else {
            model.addAttribute("signupError", signupError);
        }
        return "signup";
    }
}
