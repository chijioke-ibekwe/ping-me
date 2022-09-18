package com.project.pingme.service;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.ContactDTO;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.ChatMessageRepository;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.impl.MessageServiceImpl;
import com.project.pingme.service.impl.UserContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserContactServiceImpl.class)
class UserContactServiceImplTest {

    @Autowired
    private UserContactService userContactService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserContactRepository userContactRepository;

    private UserContact userContact;

    private ContactDTO contactDTO;

    @BeforeEach
    void setUp(){
        ChatMessage chatMessageOne = ChatMessage.builder()
                .messageText("How are you?")
                .messageTime(LocalDateTime.of(2022, 05, 12, 9, 15))
                .sender("John Doe")
                .build();

        ChatMessage chatMessageTwo = ChatMessage.builder()
                .messageText("I'm good")
                .messageTime(LocalDateTime.of(2022, 05, 12, 10, 35))
                .sender("Jane Doe")
                .build();

        userContact = UserContact.builder()
                .id(1L)
                .host(User.builder()
                        .id(2L)
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .contact(User.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .build())
                .chatMessages(Arrays.asList(chatMessageOne, chatMessageTwo))
                .build();

        contactDTO = ContactDTO.builder()
                .userContactId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    @WithMockUser
    void testGetContacts(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(userContact.getHost()));
        when(userContactRepository.findByHostOrContact(any(User.class), any(User.class))).thenReturn(Arrays.asList(userContact));

        List<ContactDTO> result = userContactService.getContacts(authentication);

        assertThat(result.get(0).getUserContactId()).isEqualTo(1L);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jane");
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");
    }
}
