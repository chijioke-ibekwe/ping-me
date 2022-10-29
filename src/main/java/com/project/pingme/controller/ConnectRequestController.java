package com.project.pingme.controller;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.dto.RequestStatusDTO;
import com.project.pingme.entity.User;
import com.project.pingme.enums.RequestStatus;
import com.project.pingme.service.ConnectRequestService;
import com.project.pingme.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@Slf4j
@Controller
public class ConnectRequestController {

    private ConnectRequestService connectRequestService;
    private UserService userService;
    private SimpMessagingTemplate messagingTemplate;

    public ConnectRequestController(ConnectRequestService connectRequestService, UserService userService,
                                    SimpMessagingTemplate messagingTemplate) {
        this.connectRequestService = connectRequestService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/request")
    @PreAuthorize("isAuthenticated()")
    public String getRequests(Authentication authentication, Model model){
        User authUser = userService.getUserByUsername(authentication.getName());
        model.addAttribute("requests", connectRequestService.getReceivedConnectRequests(authUser));
        model.addAttribute("userId", authUser.getId());
        model.addAttribute("username", authUser.getUsername());
        return "request";
    }

    @MessageMapping("/request/send")
    @PreAuthorize("isAuthenticated()")
    public void sendConnectionRequest(@RequestBody ConnectRequestDTO request, Authentication authentication){
        User authUser = userService.getUserByUsername(authentication.getName());
        log.debug("Saving connection request...");
        ConnectRequestDTO response = connectRequestService.createConnectRequest(authUser, request);
        log.debug("Connection request saved...");

        messagingTemplate.convertAndSendToUser(
                response.getRecipientId().toString(),"/queue/connections", response);
    }

    @MessageMapping("/request/{requestId}/update")
    @PreAuthorize("isAuthenticated()")
    public void updateRequestStatus(@RequestBody RequestStatusDTO requestStatus,
                                    @DestinationVariable Long requestId,
                                    Authentication authentication){
        User authUser = userService.getUserByUsername(authentication.getName());
        log.debug("Saving connection request...");
        ConnectRequestDTO response = connectRequestService.updateConnectRequestStatus(authUser, requestId, RequestStatus.valueOf(requestStatus.getStatus()));
        log.debug("Connection request saved...");

        if (Objects.nonNull(response))
            messagingTemplate.convertAndSendToUser(response.getRecipientId().toString(),"/queue/connections", response);
    }
}
