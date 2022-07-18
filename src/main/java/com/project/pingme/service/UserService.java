package com.project.pingme.service;

import com.project.pingme.entity.User;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Service
public class UserService {
    private UserRepository userRepository;
    private HashService hashService;

    public UserService(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    public Long createUser(User user){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];

        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        User user_ = userRepository.save(new User(user.getUsername(), encodedSalt, hashedPassword,
                user.getFirstName(), user.getLastName(), null, null));

        return user_.getId();
    }

    public boolean isAvailable(String username){
        return Objects.nonNull(userRepository.findByUsername(username));
    }
}
