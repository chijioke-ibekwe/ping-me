package com.project.pingme.controller;

import com.project.pingme.config.SecurityConfig;
import com.project.pingme.service.AuthenticationService;
import com.project.pingme.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
