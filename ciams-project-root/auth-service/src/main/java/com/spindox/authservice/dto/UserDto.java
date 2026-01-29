package com.spindox.authservice.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserDto {

    private String id;
    private String username;
    private String password;
    private ArrayList<String> roles;
}
