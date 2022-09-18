package com.project.pingme.service;

import com.project.pingme.dto.ContactDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserContactService {

    List<ContactDTO> getContacts(Authentication authentication);
}
