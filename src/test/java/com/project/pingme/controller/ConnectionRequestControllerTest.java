package com.project.pingme.controller;

import com.project.pingme.entity.User;
import com.project.pingme.util.TestUtil;
import com.project.pingme.config.SecurityConfig;
import com.project.pingme.service.*;
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
class ConnectionRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectRequestService connectRequestService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    @WithMockUser
    void testGetRequests() throws Exception {

        TestUtil testUtil = new TestUtil();

        when(userService.getUserByUsername(any())).thenReturn(testUtil.getUser());
        when(connectRequestService.getReceivedConnectRequests(any(User.class))).thenReturn(testUtil.getConnectRequestDTOS());

        this.mockMvc.perform(get("/request"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("requests", testUtil.getConnectRequestDTOS()))
                .andExpect(model().attribute("userId", 2L))
                .andExpect(model().attribute("username", "john.doe"))
                .andExpect(view().name("request"));

    }
}