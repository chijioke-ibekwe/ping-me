package com.project.pingme.service.impl;

import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;
import com.project.pingme.enums.UserSearchCriteria;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.HashService;
import com.project.pingme.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private HashService hashService;

    public UserServiceImpl(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
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

        User user = new User();
        user.setFirstName(signupDTO.getFirstName());
        user.setLastName(signupDTO.getLastName());
        user.setUsername(signupDTO.getUsername());
        user.setSalt(encodedSalt);
        user.setPassword(hashedPassword);
        user.setHostInstances(new ArrayList<>());
        user.setContactInstances(new ArrayList<>());

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<User> searchUsersBy(SearchUserDTO searchDTO){
        if(searchDTO.getSearchCriteria().equals(UserSearchCriteria.BY_NAME)){
            return userRepository.findByFirstNameOrLastNameContainingIgnoreCase(searchDTO.getSearchInput());
        }else if(searchDTO.getSearchCriteria().equals(UserSearchCriteria.BY_PHONE_NUMBER)){
            return userRepository.findByPhoneNumberContainingIgnoreCase(searchDTO.getSearchInput());
        }else if(searchDTO.getSearchCriteria().equals(UserSearchCriteria.BY_USERNAME)){
            return userRepository.findByUsernameContainingIgnoreCase(searchDTO.getSearchInput());
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isAvailable(String username){

        return userRepository.existsByUsername(username);
    }
}
