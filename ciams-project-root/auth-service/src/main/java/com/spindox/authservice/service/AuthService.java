package com.spindox.authservice.service;

import com.spindox.authservice.dto.AuthRequest;
import com.spindox.authservice.dto.AuthResponse;
import com.spindox.authservice.dto.UserDto;
import com.spindox.authservice.model.User;
import com.spindox.authservice.repository.UserRepository;
import com.spindox.authservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public void register(UserDto userDto) throws Exception {
        Optional<User> existingUser = repository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(String.format("User with the username '%s' already exists.", userDto.getUsername()));
        }

        String hashedPassword = encoder.encode(userDto.getPassword());
        User user = User.builder()
                .username(userDto.getUsername())
                .password(hashedPassword)
                .roles(userDto.getRoles())
                .build();
        repository.save(user);
    }

    public AuthResponse login(AuthRequest authRequest) throws Exception {
        User user = repository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!encoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtUtils.generateToken(user.getUsername(), user.getRoles());
        return new AuthResponse(token);
    }
}
