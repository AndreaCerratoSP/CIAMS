package com.spindox.authservice.model;

import lombok.Data;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@Document(collection = "User")
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private ArrayList<String> roles;

    public User(String username, ArrayList<String> roles, String hashedPassword) {
    }
}