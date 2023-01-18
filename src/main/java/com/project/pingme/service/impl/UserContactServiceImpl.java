package com.project.pingme.service.impl;

import com.project.pingme.dto.ConnectRequestDTO;
import com.project.pingme.dto.ContactDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.service.UserContactService;
import com.project.pingme.service.UserService;
import com.project.pingme.util.Formatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class UserContactServiceImpl implements UserContactService {
    private UserService userService;
    private UserContactRepository userContactRepository;

    public UserContactServiceImpl(UserService userService, UserContactRepository userContactRepository) {
        this.userService = userService;
        this.userContactRepository = userContactRepository;
    }

    @Override
    public UserContact getContactById(Long userContactId){
        return userContactRepository.findById(userContactId).orElseThrow(() ->
                new EntityNotFoundException("Contact not found"));
    }

    @Override
    public List<ContactDTO> getContactDTOS(User authUser){

        List<UserContact> userContacts = userContactRepository.findByHostOrContact(authUser, authUser);
        List<ContactDTO> contacts = new ArrayList<>();

        userContacts.forEach(uc -> {
            ContactDTO contact;
            if (uc.getHost().getId().equals(authUser.getId())){
                contact = ContactDTO.builder()
                        .userContactId(uc.getId())
                        .contactId(uc.getContact().getId())
                        .firstName(uc.getContact().getFirstName())
                        .lastName(uc.getContact().getLastName())
                        .displayPictureUrl(uc.getContact().getDisplayPictureUrl())
                        .build();
            }else{
                contact = ContactDTO.builder()
                        .userContactId(uc.getId())
                        .contactId(uc.getHost().getId())
                        .firstName(uc.getHost().getFirstName())
                        .lastName(uc.getHost().getLastName())
                        .displayPictureUrl(uc.getHost().getDisplayPictureUrl())
                        .build();
            }
            contacts.add(contact);
        });

        return contacts;
    }

    @Transactional
    @Override
    public ConnectRequestDTO createContact(User authUser, User requestSender){

        UserContact userContact = new UserContact();
        userContact.setContact(authUser);
        userContact.setHost(requestSender);

        userContact = userContactRepository.saveAndFlush(userContact);

        return ConnectRequestDTO.builder()
                .recipientId(userContact.getHost().getId())
                .recipientName(Formatter.formatUserFullName(userContact.getHost()))
                .senderName(Formatter.formatUserFullName(userContact.getContact()))
                .activity("ACCEPTED_YOUR_REQUEST")
                .build();
    }
}
