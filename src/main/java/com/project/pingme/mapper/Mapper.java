package com.project.pingme.mapper;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.ConnectRequest;
import com.project.pingme.entity.User;
import com.project.pingme.util.Formatter;

public class Mapper {

    public static UserDTO mapToUserDTO(User user){

        return UserDTO.builder()
                .id(user.getId())
                .fullName(Formatter.formatUserFullName(user))
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static ConnectRequestDTO mapToConnectRequestDTO(ConnectRequest connectRequest){

        return ConnectRequestDTO.builder()
                .requestId(connectRequest.getId())
                .recipientName(Formatter.formatUserFullName(connectRequest.getRecipient()))
                .senderName(Formatter.formatUserFullName(connectRequest.getSender()))
                .recipientId(connectRequest.getRecipient().getId())
                .build();
    }
}
