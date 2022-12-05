package com.project.pingme.service.impl;

import com.project.pingme.dto.SearchUserDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.dto.UpdateUserDTO;
import com.project.pingme.dto.UserDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.enums.RequestStatus;
import com.project.pingme.enums.UserSearchCriteria;
import com.project.pingme.repository.ConnectRequestRepository;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import com.project.pingme.service.HashService;
import com.project.pingme.service.UserService;
import com.project.pingme.util.Formatter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ConnectRequestRepository connectRequestRepository;
    private UserContactRepository userContactRepository;
    private HashService hashService;

    public UserServiceImpl(UserRepository userRepository, ConnectRequestRepository connectRequestRepository,
                           UserContactRepository userContactRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.connectRequestRepository = connectRequestRepository;
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

        User user = new User();
        user.setFirstName(signupDTO.getFirstName());
        user.setLastName(signupDTO.getLastName());
        user.setUsername(signupDTO.getUsername());
        user.setSalt(encodedSalt);
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public List<UserDTO> searchUsersBy(User authUser, SearchUserDTO searchDTO){
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOS = new ArrayList<>();
        if(searchDTO.getSearchCriteria().equals(UserSearchCriteria.BY_NAME)){
            users = userRepository.findByFirstNameOrLastNameContainingIgnoreCase(searchDTO.getSearchInput());
        }else if(searchDTO.getSearchCriteria().equals(UserSearchCriteria.BY_PHONE_NUMBER)){
            users = userRepository.findByPhoneNumberContainingIgnoreCase(searchDTO.getSearchInput());
        }else if(searchDTO.getSearchCriteria().equals(UserSearchCriteria.BY_USERNAME)) {
            users = userRepository.findByUsernameContainingIgnoreCase(searchDTO.getSearchInput());
        }

        users.forEach(user -> {
            boolean requestSent = connectRequestRepository.existsBySenderAndRecipientAndRequestStatus(authUser, user,
                    RequestStatus.PENDING);
            UserContact userContact = userContactRepository.findByHostAndContact(authUser.getId(), user.getId());

            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .fullName(Formatter.formatUserFullName(user))
                    .username(user.getUsername())
                    .phoneNumber(user.getPhoneNumber())
                    .requestSent(requestSent)
                    .contact(Objects.nonNull(userContact))
                    .build();

            if(!authUser.equals(user))
                userDTOS.add(userDTO);
        });

        return userDTOS;
    }

    @Override
    public boolean isAvailable(String username){

        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User updateUserProfile(User authUser, UpdateUserDTO updateUserDTO){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true).setMatchingStrategy(Matc);
    }
}
