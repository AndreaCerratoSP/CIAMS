package com.spindox.authservice.controller;

import com.spindox.authservice.dto.UserDto;
import com.spindox.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto) throws Exception {
        userService.saveUser(userDto);
        return ResponseEntity.ok(userDto);
    }
}
