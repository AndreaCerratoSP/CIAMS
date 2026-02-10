package com.spindox.authservice.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDto {
    private String id;
    private String username;
    private String password;
    private List<String> roles;
}
