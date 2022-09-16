package com.project.pingme.service;

import com.project.pingme.dto.SignupDTO;

public interface UserService {

    Long createUser(SignupDTO signupDTO);

    boolean isAvailable(String username);
}
