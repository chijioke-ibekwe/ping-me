package com.project.pingme.controller;

import com.project.pingme.config.SecurityConfig;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.service.AuthenticationService;
import com.project.pingme.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @Test
    void testAccessToLoginPageForAnonymousUsers() throws Exception {
        this.mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testAccessToSignUpPageForAnonymousUsers() throws Exception {
        this.mockMvc.perform(get("/user/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    void testUserSignUp() throws Exception {

        when(userService.isAvailable(any())).thenReturn(false);
        when(userService.createUser(any(SignupDTO.class))).thenReturn(1L);

        this.mockMvc.perform(post("/user/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "john.doe")
                        .param("password", "password")
                        .param("confirmPassword","password")
                        .with(csrf()))
                        .andExpect(status().isOk())
                        .andExpect(model().attribute("signupSuccess", true))
                        .andExpect(model().attribute("successMessage", "Sign Up Successful!"));
    }
}
