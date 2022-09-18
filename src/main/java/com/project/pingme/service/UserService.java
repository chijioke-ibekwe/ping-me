package com.project.pingme.service;

import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;

public interface UserService {

    Long createUser(SignupDTO signupDTO);

    User getUserByUsername(String username);

    boolean isAvailable(String username);
}
