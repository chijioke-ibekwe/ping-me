package com.project.pingme.service.impl;

import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;
    private HashServiceImpl hashService;

    public UserServiceImpl(UserRepository userRepository, UserContactRepository userContactRepository,
                           HashServiceImpl hashService) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.hashService = hashService;
    }

    @Transactional
    @Override
    public Long createUser(SignupDTO signupDTO){

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];

        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(signupDTO.getPassword(), encodedSalt);

        User _user = userRepository.save(new User(signupDTO.getUsername(), encodedSalt, hashedPassword,
                signupDTO.getFirstName(), signupDTO.getLastName(), new ArrayList<>(), new ArrayList<>()));

        return _user.getId();
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public boolean isAvailable(String username){

        return userRepository.existsByUsername(username);
    }
}
