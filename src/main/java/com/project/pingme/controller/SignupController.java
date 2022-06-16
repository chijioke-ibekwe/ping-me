package com.project.pingme.controller;

import com.project.pingme.dto.SignupForm;
import com.project.pingme.dto.User;
import com.project.pingme.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignUpPage(@ModelAttribute("signupForm") SignupForm signupForm, Model model){
        return "signup";
    }

    @PostMapping
    public String createAccount(@ModelAttribute("signupForm") SignupForm signupForm,
                                Model model){
        User user = new User(null, signupForm.getUsername(), null, signupForm.getPassword(), signupForm.getFirstName(),
                signupForm.getLastName());

        String signupError = null;

        if(userService.isAvailable(signupForm.getUsername())){
            signupError = "Username already exists";
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 1) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupSuccess", true);
            model.addAttribute("successMessage", "Sign Up Successful!");
        } else {
            model.addAttribute("signupError", signupError);
        }
        return "signup";
    }
}
