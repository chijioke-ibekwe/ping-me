package com.project.pingme.service;

import com.project.pingme.dto.User;
import com.project.pingme.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
public class UserService {
    private UserMapper userMapper;
    private HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public int createUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];

        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        return userMapper.insert(new User(null, user.getUsername(), encodedSalt, hashedPassword,
                user.getFirstName(), user.getLastName()));
    }

    public boolean isAvailable(String username){
        return Objects.nonNull(userMapper.getUser(username));
    }
}
