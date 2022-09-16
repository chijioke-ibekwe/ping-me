package com.project.pingme.service;

import com.project.pingme.dto.ContactDTO;

import java.util.List;

public interface UserContactService {

    List<ContactDTO> getContacts(String username);
}
