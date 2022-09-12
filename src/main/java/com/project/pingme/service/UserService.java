package com.project.pingme.service;

import com.project.pingme.dto.ContactDTO;
import com.project.pingme.dto.SignupDTO;
import com.project.pingme.entity.User;
import com.project.pingme.entity.UserContact;
import com.project.pingme.repository.UserContactRepository;
import com.project.pingme.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;
    private HashService hashService;

    public UserService(UserRepository userRepository, UserContactRepository userContactRepository,
                       HashService hashService) {
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.hashService = hashService;
    }

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

    public boolean isAvailable(String username){

        return userRepository.existsByUsername(username);
    }

    public List<ContactDTO> getContacts(String username){
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isPresent()){
            List<UserContact> userContacts = userContactRepository.findByHostOrContact(user.get(), user.get());
            List<ContactDTO> contacts = new ArrayList<>();
            userContacts.forEach(uc -> {
                if (uc.getHost().getId().equals(user.get().getId())){
                    ContactDTO contact = ContactDTO.builder()
                            .userContactId(uc.getId())
                            .firstName(uc.getContact().getFirstName())
                            .lastName(uc.getContact().getLastName())
                            .build();
                    contacts.add(contact);
                }else{
                    ContactDTO contact = ContactDTO.builder()
                            .userContactId(uc.getId())
                            .firstName(uc.getHost().getFirstName())
                            .lastName(uc.getHost().getLastName())
                            .build();
                    contacts.add(contact);
                }
            });

            return contacts;

        }else{
            throw new EntityNotFoundException("user not found");
        }
    }
}
