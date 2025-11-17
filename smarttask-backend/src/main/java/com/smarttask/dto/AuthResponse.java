package com.smarttask.dto;

public class AuthResponse {
    
    public String token;
    public UserDTO user;

    public AuthResponse() {}

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
