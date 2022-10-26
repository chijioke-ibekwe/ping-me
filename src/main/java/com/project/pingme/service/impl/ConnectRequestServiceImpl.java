package com.project.pingme.service.impl;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.ConnectRequest;
import com.project.pingme.entity.User;
import com.project.pingme.enums.RequestStatus;
import com.project.pingme.mapper.Mapper;
import com.project.pingme.repository.ConnectRequestRepository;
import com.project.pingme.service.ConnectRequestService;
import com.project.pingme.service.UserContactService;
import com.project.pingme.service.UserService;
import com.project.pingme.util.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectRequestServiceImpl implements ConnectRequestService {

    private ConnectRequestRepository connectRequestRepository;
    private UserContactService userContactService;
    private UserService userService;

    public ConnectRequestServiceImpl(ConnectRequestRepository connectRequestRepository,
                                     UserContactService userContactService, UserService userService) {
        this.connectRequestRepository = connectRequestRepository;
        this.userContactService = userContactService;
        this.userService = userService;
    }

    @Override
    public List<ConnectRequest> getSentConnectRequests(User authUser){
        return connectRequestRepository.findBySenderAndRequestStatus(authUser.getId(), RequestStatus.PENDING.name(),
                RequestStatus.REJECTED.name());
    }

    @Override
    public List<ConnectRequestDTO> getReceivedConnectRequests(User authUser){
        List<ConnectRequest> requests = connectRequestRepository.findByRecipientAndRequestStatus(authUser, RequestStatus.PENDING);

        return requests.stream().map(Mapper::mapToConnectRequestDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ConnectRequestDTO createConnectRequest(User authUser, ConnectRequestDTO connectRequestDTO){
        User user = userService.getUserById(connectRequestDTO.getRecipientId());

        ConnectRequest connectRequest = new ConnectRequest();
        connectRequest.setSender(authUser);
        connectRequest.setRecipient(user);
        connectRequest.setRequestStatus(RequestStatus.PENDING);

        ConnectRequest savedRequest = connectRequestRepository.saveAndFlush(connectRequest);

        connectRequestDTO.setRecipientName(Formatter.formatUserFullName(savedRequest.getRecipient()));
        connectRequestDTO.setSenderName(Formatter.formatUserFullName(savedRequest.getSender()));
        connectRequestDTO.setActivity("RECEIVED_REQUEST");

        return connectRequestDTO;
    }

    @Transactional
    @Override
    public ConnectRequestDTO updateConnectRequest(User authUser, Long connectRequestId, RequestStatus requestStatus){
        ConnectRequest connectRequest = connectRequestRepository.findByRecipientAndId(authUser, connectRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        connectRequest.setRequestStatus(requestStatus);
        ConnectRequestDTO addedContact;

        if(requestStatus.equals(RequestStatus.ACCEPTED)) {
            addedContact = userContactService.createContact(authUser, connectRequest.getSender());
            connectRequestRepository.save(connectRequest);
            return addedContact;
        }

        connectRequestRepository.save(connectRequest);
        return null;
    }
}
