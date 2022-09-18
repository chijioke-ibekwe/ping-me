package com.project.pingme.service;

import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserServiceImpl.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private HashService hashService;

    @MockBean
    private UserRepository userRepository;

    private SignupDTO signupDTO;

    private User user;

    @BeforeEach
    void setUp(){

        signupDTO = SignupDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("password")
                .confirmPassword("password")
                .build();

        user = User.builder()
                .id(1L)
                .build();
    }

    @Test
    void testCreateUser(){
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(hashService.getHashedValue(any(), any())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(signupDTO);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertThat(userArgumentCaptor.getValue().getFirstName()).isEqualTo("John");
        assertThat(userArgumentCaptor.getValue().getLastName()).isEqualTo("Doe");
        assertThat(userArgumentCaptor.getValue().getPassword()).isEqualTo("hashedPassword");
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo("john.doe");
    }

    @Test
    void testGetUserByUsername(){
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("username");

        assertThat(result).isNotNull();
    }

    @Test
    void testIsAvailable(){
        when(userRepository.existsByUsername(any())).thenReturn(true);

        boolean result = userService.isAvailable("username");

        assertThat(result).isTrue();
    }
}
