package com.project.pingme.service;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.ConnectRequest;
import com.project.pingme.entity.User;
import com.project.pingme.enums.RequestStatus;

import java.util.List;

public interface ConnectRequestService {

    List<ConnectRequest> getSentConnectRequests(User authUser);

    List<ConnectRequestDTO> getReceivedConnectRequests(User authUser);

    ConnectRequestDTO createConnectRequest(User authUser, ConnectRequestDTO connectRequestDTO);

    ConnectRequestDTO updateConnectRequestStatus(User authUser, Long connectRequestId, RequestStatus requestStatus);
}
