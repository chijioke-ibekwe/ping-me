package com.project.pingme.service;

import com.project.pingme.dto.User;
import com.project.pingme.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {
    private UserMapper userMapper;
    private HashService hashService;

    public AuthenticationService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    @Override
    public Authentication authenticate(Authentication authentication){
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userMapper.getUser(username);
        if(user != null){
            String encodedSalt = user.getSalt();
            String hashedPassword = hashService.getHashedValue(password, encodedSalt);
            if(user.getPassword().equals(hashedPassword)){
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication){
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
