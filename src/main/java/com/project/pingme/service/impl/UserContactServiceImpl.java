package com.project.pingme.service.impl;

import com.project.pingme.dto.ContactDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.enums.RequestStatus;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.UserContactService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class UserContactServiceImpl implements UserContactService {
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;

    public UserContactServiceImpl(UserRepository userRepository, UserContactRepository userContactRepository) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
    }

    @Override
    public List<ContactDTO> getContacts(Authentication authentication){
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() ->
                new EntityNotFoundException("user not found"));

        List<UserContact> userContacts = userContactRepository.findByHostOrContactAndRequestStatus(user, user, RequestStatus.ACCEPTED);
        List<ContactDTO> contacts = new ArrayList<>();

        userContacts.forEach(uc -> {
            ContactDTO contact;
            if (uc.getHost().getId().equals(user.getId())){
                contact = ContactDTO.builder()
                        .userContactId(uc.getId())
                        .firstName(uc.getContact().getFirstName())
                        .lastName(uc.getContact().getLastName())
                        .build();
            }else{
                contact = ContactDTO.builder()
                        .userContactId(uc.getId())
                        .firstName(uc.getHost().getFirstName())
                        .lastName(uc.getHost().getLastName())
                        .build();
            }
            contacts.add(contact);
        });

        return contacts;
    }
}
