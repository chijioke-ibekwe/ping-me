package com.project.pingme.service;

import com.project.pingme.dto.ChatDTO;
import com.project.pingme.dto.MessageDTO;
import com.project.pingme.entity.ChatMessage;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.ChatMessageRepository;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.service.impl.MessageServiceImpl;
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
@ContextConfiguration(classes = MessageServiceImpl.class)
class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserContactRepository userContactRepository;

    @MockBean
    private ChatMessageRepository chatMessageRepository;

    private UserContact userContact;

    private ChatDTO chatDTO;

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

        chatDTO = ChatDTO.builder()
                .messageText("What's up?")
                .userContactId(1L)
                .username("john.doe")
                .build();
    }
    
    @Test
    @WithMockUser
    void testGetMessages(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        when(userContactRepository.findById(any())).thenReturn(Optional.of(userContact));
        when(userService.getUserByUsername(any())).thenReturn(userContact.getHost());

        List<MessageDTO> result = messageService.getMessages(authentication, 1L);

        assertThat(result.get(0).getUserContactId()).isEqualTo(1L);
        assertThat(result.get(0).getUserFullName()).isEqualTo("John Doe");
        assertThat(result.get(0).getMessageText()).isEqualTo("How are you?");
        assertThat(result.get(0).getMessageTime()).isEqualTo("12-05 09:15");
        assertThat(result.get(0).getSender()).isEqualTo("John Doe");

        assertThat(result.get(1).getUserContactId()).isEqualTo(1L);
        assertThat(result.get(1).getUserFullName()).isEqualTo("John Doe");
        assertThat(result.get(1).getMessageText()).isEqualTo("I'm good");
        assertThat(result.get(1).getMessageTime()).isEqualTo("12-05 10:35");
        assertThat(result.get(1).getSender()).isEqualTo("Jane Doe");
    }

    @Test
    @WithMockUser
    void testAddMessage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ArgumentCaptor<ChatMessage> messageArgumentCaptor = ArgumentCaptor.forClass(ChatMessage.class);

        when(userContactRepository.findById(any())).thenReturn(Optional.of(userContact));
        when(userService.getUserByUsername(any())).thenReturn(userContact.getHost());

        messageService.addMessage(authentication, chatDTO);

        verify(chatMessageRepository, times(1)).save(messageArgumentCaptor.capture());

        assertThat(messageArgumentCaptor.getValue().getMessageText()).isEqualTo("What's up?");
        assertThat(messageArgumentCaptor.getValue().getUserContact()).isEqualTo(userContact);
        assertThat(messageArgumentCaptor.getValue().getSender()).isEqualTo("John Doe");
    }
}
