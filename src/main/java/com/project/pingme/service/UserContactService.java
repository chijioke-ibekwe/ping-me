package com.project.pingme.service;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.dto.ContactDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;

import java.util.List;

public interface UserContactService {

    UserContact getContactById(Long userContactId);

    List<ContactDTO> getContactDTOS(User authUser);

    ConnectRequestDTO createContact(User authUser, User requestSender);
}
