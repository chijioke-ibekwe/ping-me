package com.project.pingme.controller;

import com.project.pingme.TestUtil;
import com.project.pingme.config.SecurityConfig;
import com.project.pingme.entity.User;
import com.project.pingme.service.AuthenticationService;
import com.project.pingme.service.UserContactService;
import com.project.pingme.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserContactService userContactService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @WithMockUser
    void testGetContacts() throws Exception {

        TestUtil testUtil = new TestUtil();

        when(userService.getUserByUsername(any())).thenReturn(testUtil.getUser());
        when(userContactService.getContactDTOS(any(User.class))).thenReturn(testUtil.getContactDTOS());

        this.mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contacts", testUtil.getContactDTOS()))
                .andExpect(model().attribute("userId", 2L))
                .andExpect(model().attribute("username", "john.doe"))
                .andExpect(view().name("contact"));

    }
}