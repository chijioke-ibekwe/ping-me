package com.project.pingme.service;

import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.dto.UpdateUserDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.User;

import java.util.List;

public interface UserService {

    Long createUser(SignupDTO signupDTO);

    User getUserByUsername(String username);

    User getUserById(Long userId);

    List<UserDTO> searchUsersBy(User authUser, SearchUserDTO searchDTO);

    boolean isAvailable(String username);

    User updateUserProfile(User authUser, UpdateUserDTO updateUserDTO) throws Exception;
}
