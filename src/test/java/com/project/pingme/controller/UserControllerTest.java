package com.project.pingme.controller;

import com.project.pingme.config.SecurityConfig;
import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.User;
import com.project.pingme.service.AuthenticationService;
import com.project.pingme.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserService userService;

    @Test
    void testAccessToLoginPageForAnonymousUsers() throws Exception {
        this.mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    void testAccessToSignUpPageForAnonymousUsers() throws Exception {
        this.mockMvc.perform(get("/user/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }

    @Test
    void testUserSignUp() throws Exception {

        when(userService.isAvailable(any())).thenReturn(false);
        when(userService.createUser(any(SignupDTO.class))).thenReturn(1L);

        this.mockMvc.perform(post("/user/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "john.doe")
                        .param("password", "password")
                        .param("confirmPassword","password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("signupSuccess", true))
                .andExpect(model().attribute("successMessage", "Sign Up Successful!"));
    }

    @Test
    void testUserSignUp_whenUserExists() throws Exception {

        when(userService.isAvailable(any())).thenReturn(true);

        this.mockMvc.perform(post("/user/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "john.doe")
                        .param("password", "password")
                        .param("confirmPassword","password")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("signupError", "Username already exists"));
    }

    @Test
    void testUserSignUp_whenPasswordsDoNotMatch() throws Exception {

        when(userService.isAvailable(any())).thenReturn(true);

        this.mockMvc.perform(post("/user/signup")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("username", "john.doe")
                        .param("password", "password")
                        .param("confirmPassword","passport")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("signupError", "Password fields have to match"));
    }

    @Test
    void testUserSearchPage_redirectAnonymousUserToLogin() throws Exception {

        this.mockMvc.perform(post("/user/search")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    void testUserSearch() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(3L)
                .fullName("Jane Doe")
                .username("jane.doe")
                .phoneNumber("+2348000002222")
                .build();

        when(userService.getUserByUsername(any())).thenReturn(User.builder().id(1L).username("jon.doe").build());
        when(userService.searchUsersBy(any(User.class), any(SearchUserDTO.class))).thenReturn(Collections.singletonList(user));

        this.mockMvc.perform(get("/user/search-user")
                        .param("searchInput", "John")
                        .param("searchCriteria", "BY_NAME")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/find"))
                .andExpect(model().attribute("users", Collections.singletonList(user)));
    }


    @Test
    @WithMockUser
    void testUserSearch_whenNoSearchInputIsProvided() throws Exception {

        when(userService.getUserByUsername(any())).thenReturn(User.builder().id(1L).username("jon.doe").build());
        verify(userService, times(0)).searchUsersBy(any(User.class), any(SearchUserDTO.class));

        this.mockMvc.perform(get("/user/search-user")
                        .param("searchInput", "")
                        .param("searchCriteria", "BY_NAME")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/find"));
    }

}
