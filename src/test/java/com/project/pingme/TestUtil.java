package com.project.pingme;

import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public UserContact getUserContact(){

        return UserContact.builder()
                .id(1L)
                .host(User.builder()
                        .id(2L)
                        .firstName("John")
                        .lastName("Doe")
                        .username("john.doe")
                        .build())
                .contact(User.builder()
                        .id(3L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .username("jane.doe")
                        .build())
                .chatMessages(new ArrayList<>())
                .build();
    }

    public User getUser(){

        return User.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .build();
    }

    public List<MessageDTO> getMessages(){

        MessageDTO messageOne = MessageDTO.builder()
                .messageId(1L)
                .messageText("How are you?")
                .userContactId(4L)
                .sender("John Doe")
                .userFullName("John Doe")
                .build();

        MessageDTO messageTwo = MessageDTO.builder()
                .messageId(1L)
                .messageText("I'm alright")
                .userContactId(5L)
                .sender("Jane Doe")
                .userFullName("John Doe")
                .build();

        return Arrays.asList(messageOne, messageTwo);
    }
}
