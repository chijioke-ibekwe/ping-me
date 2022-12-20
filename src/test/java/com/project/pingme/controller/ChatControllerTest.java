package com.project.pingme.controller;

import com.project.pingme.util.TestUtil;
import com.project.pingme.config.SecurityConfig;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.User;
import com.project.pingme.service.AuthenticationService;
import com.project.pingme.service.MessageService;
import com.project.pingme.service.UserContactService;
import com.project.pingme.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserContactService userContactService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    @WithMockUser
    void testGetChat() throws Exception {

        TestUtil testUtil = new TestUtil();

        when(userService.getUserByUsername(any())).thenReturn(testUtil.getUser());
        when(userContactService.getContactById(anyLong())).thenReturn(testUtil.getUserContact());
        when(messageService.getMessages(any(User.class), anyLong())).thenReturn(testUtil.getMessages());

        this.mockMvc.perform(get("/chat/{userContactId}", 3L))
                .andExpect(status().isOk())
                .andExpect(model().attribute("messages", testUtil.getMessages()))
                .andExpect(model().attribute("user", testUtil.getUser()))
                .andExpect(model().attribute("userContactId", 3L))
                .andExpect(model().attribute("contact", UserDTO.builder()
                        .id(3L)
                        .fullName("Jane Doe")
                        .username("jane.doe").build()))
                .andExpect(view().name("chat"));

    }
}