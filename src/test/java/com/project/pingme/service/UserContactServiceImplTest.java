package com.project.pingme.service;

import com.project.pingme.dto.ContactDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.service.impl.UserContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserContactServiceImpl.class)
class UserContactServiceImplTest {

    @Autowired
    private UserContactService userContactService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserContactRepository userContactRepository;

    private UserContact userContact;

    private ContactDTO contactDTO;

    private User authUser;

    @BeforeEach
    void setUp(){

        userContact = UserContact.builder()
                .id(1L)
                .host(User.builder()
                        .id(2L)
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .contact(User.builder()
                        .id(3L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .build())
                .chatMessages(new ArrayList<>())
                .build();

        contactDTO = ContactDTO.builder()
                .userContactId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        authUser.setId(2L);
    }

    @Test
    @WithMockUser
    void testGetContacts(){
        when(userService.getUserByUsername(any())).thenReturn(userContact.getHost());
        when(userContactRepository.findByHostOrContact(any(User.class), any(User.class))).thenReturn(Arrays.asList(userContact));

        List<ContactDTO> result = userContactService.getContactDTOS(authUser);

        assertThat(result.get(0).getUserContactId()).isEqualTo(1L);
        assertThat(result.get(0).getFirstName()).isEqualTo("Jane");
        assertThat(result.get(0).getLastName()).isEqualTo("Doe");
    }
}
