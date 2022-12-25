package com.project.pingme.controller;

import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.dto.UpdateUserDTO;
import com.project.pingme.entity.User;
import com.project.pingme.enums.UserSearchCriteria;
import com.project.pingme.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Objects;

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
        return "user/login";
    }

    @GetMapping("/logout")
    public String logoutUser(){
        return "user/login";
    }

    @GetMapping("/signup")
    public String getSignUpPage(@ModelAttribute("signupDTO") SignupDTO signupDTO, Model model){
        return "user/signup";
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
        return "user/signup";
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public String searchUserPage(@ModelAttribute("searchDTO") SearchUserDTO searchDTO, Authentication authentication,
                               Model model){
        User user = userService.getUserByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "user/find";
    }

    @GetMapping("/search-user")
    @PreAuthorize("isAuthenticated()")
    public String searchUser(@ModelAttribute("searchDTO") SearchUserDTO searchDTO, Authentication authentication,
                             Model model){
        User authUser = userService.getUserByUsername(authentication.getName());
        if(Arrays.stream(UserSearchCriteria.values()).anyMatch(sc -> sc.equals(searchDTO.getSearchCriteria())) &&
                !searchDTO.getSearchInput().isEmpty()){
            model.addAttribute("users", userService.searchUsersBy(authUser, searchDTO));
        }
        model.addAttribute("user", authUser);
        return "user/find";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String getProfilePage(@ModelAttribute("updateUserDTO") UpdateUserDTO updateUserDTO, Authentication authentication,
                                 Model model){
        User user = userService.getUserByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/profile-update")
    @PreAuthorize("isAuthenticated()")
    public String updateUserProfile(@ModelAttribute("updateUserDTO") UpdateUserDTO updateUserDTO, Authentication authentication,
                                    Model model) throws Exception {
        User authUser = userService.getUserByUsername(authentication.getName());

        String updateError = null;
        if(Objects.nonNull(updateUserDTO.getUsername()) && userService.isAvailable(updateUserDTO.getUsername())){
            updateError = "Username already exists";
        }

        if (Objects.isNull(updateError)){
            userService.updateUserProfile(authUser, updateUserDTO);
        }

        model.addAttribute("user", authUser);
        return "user/profile";
    }
}
