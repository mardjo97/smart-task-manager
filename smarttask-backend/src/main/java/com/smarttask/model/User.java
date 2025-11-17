package com.smarttask.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@MongoEntity(collection = "users")
public class User {
    
    public ObjectId id;
    public String username;
    public String email;
    public String password; // Hashed password
    public String role;
    public LocalDateTime createdAt;
    public LocalDateTime lastLogin;

    public User() {
        this.createdAt = LocalDateTime.now();
        this.role = "USER";
    }

    public User(String username, String email, String password) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
