package com.project.pingme.controller;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;
import com.project.pingme.enums.UserSearchCriteria;
import com.project.pingme.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public String findUserPage(@ModelAttribute("searchDTO") SearchUserDTO searchDTO, Model model){
        return "find";
    }

    @GetMapping("/search-user")
    @PreAuthorize("isAuthenticated()")
    public String findUser(@ModelAttribute("searchDTO") SearchUserDTO searchDTO, Model model){

        if(Arrays.stream(UserSearchCriteria.values()).anyMatch(sc -> sc.equals(searchDTO.getSearchCriteria())) &&
                !searchDTO.getSearchInput().isEmpty()){
            model.addAttribute("users", userService.searchUsersBy(searchDTO));
        }
        return "find";
    }
}
