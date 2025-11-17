package com.smarttask.service;

import com.smarttask.dto.AuthResponse;
import com.smarttask.dto.LoginRequest;
import com.smarttask.dto.RegisterRequest;
import com.smarttask.dto.UserDTO;
import com.smarttask.model.User;
import com.smarttask.repository.UserRepository;
import com.smarttask.util.PasswordUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @ConfigProperty(name = "app.jwt.duration")
    Long jwtDuration;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username)) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email)) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.username = request.username;
        user.email = request.email;
        user.password = PasswordUtil.hashPassword(request.password);
        user.role = "USER";

        userRepository.persist(user);

        String token = generateToken(user);
        UserDTO userDTO = toDTO(user);

        return new AuthResponse(token, userDTO);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username)
                .orElseThrow(() -> new NotAuthorizedException("Invalid credentials"));

        if (!PasswordUtil.verifyPassword(request.password, user.password)) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        user.lastLogin = LocalDateTime.now();
        userRepository.update(user);

        String token = generateToken(user);
        UserDTO userDTO = toDTO(user);

        return new AuthResponse(token, userDTO);
    }

    private String generateToken(User user) {
        Set<String> roles = new HashSet<>();
        roles.add(user.role);

        return Jwt.issuer("https://smarttask.com")
                .upn(user.username)
                .groups(roles)
                .claim("userId", user.id.toString())
                .claim("email", user.email)
                .expiresIn(jwtDuration)
                .sign();
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.id != null ? user.id.toString() : null,
                user.username,
                user.email,
                user.role
        );
    }
}
