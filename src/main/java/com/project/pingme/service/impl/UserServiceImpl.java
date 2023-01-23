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
import com.project.pingme.service.AmazonService;
import com.project.pingme.service.HashService;
import com.project.pingme.service.UserService;
import com.project.pingme.util.Formatter;
import org.modelmapper.AbstractCondition;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.*;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConnectRequestRepository connectRequestRepository;
    private final UserContactRepository userContactRepository;
    private final HashService hashService;
    private final AmazonService amazonService;

    Condition<?, ?> isStringBlank = new AbstractCondition<Object, Object>() {
        @Override
        public boolean applies(MappingContext<Object, Object> context) {
            if(context.getSource() instanceof String) {
                return null!=context.getSource() && !"".equals(context.getSource());
            } else {
                return context.getSource() != null;
            }
        }
    };


    public UserServiceImpl(UserRepository userRepository, ConnectRequestRepository connectRequestRepository,
                           UserContactRepository userContactRepository, HashService hashService,
                           AmazonService amazonService) {
        this.userRepository = userRepository;
        this.connectRequestRepository = connectRequestRepository;
        this.userContactRepository = userContactRepository;
        this.hashService = hashService;
        this.amazonService = amazonService;
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
                    .displayPictureUrl(user.getDisplayPictureUrl())
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
    public User updateUserProfile(User authUser, UpdateUserDTO updateUserDTO) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPropertyCondition(isStringBlank)
                .setMatchingStrategy(STRICT);

        modelMapper.map(updateUserDTO, authUser);

        if(!updateUserDTO.getBase64Image().isEmpty()) {
            String url = amazonService.uploadPic(null, updateUserDTO.getBase64Image());
            authUser.setDisplayPictureUrl(url);
        }

        return userRepository.save(authUser);
    }
}
