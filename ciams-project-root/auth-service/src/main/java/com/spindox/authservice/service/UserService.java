package com.spindox.authservice.service;

import com.spindox.authservice.dto.UserDto;
import com.spindox.authservice.mapper.UserMapper;
import com.spindox.authservice.model.User;
import com.spindox.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserMapper mapper;

    @Transactional
    public void saveUser(UserDto userDto) throws Exception {
        String username = mapper.FromDto(userDto).getUsername();
        Optional<User> existingUser = repository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new Exception(String.format("User with the email address '%s' already exists.", username));
        }

        String hashedPassword = encoder.encode(userDto.getPassword());
        User user = new User(userDto.getUsername(), userDto.getRoles(), hashedPassword);
        repository.save(user);
    }



}
