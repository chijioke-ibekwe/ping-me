package com.project.pingme.service;

import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;

import java.util.List;

public interface UserService {

    Long createUser(SignupDTO signupDTO);

    User getUserByUsername(String username);

    List<User> searchUsersBy(SearchUserDTO searchDTO);

    boolean isAvailable(String username);
}
