package com.tccd.ecommerce_backend.controller;

import com.tccd.ecommerce_backend.dto.AuthResponse;
import com.tccd.ecommerce_backend.dto.LoginRequest;
import com.tccd.ecommerce_backend.dto.SignupRequest;
import com.tccd.ecommerce_backend.model.Role;
import com.tccd.ecommerce_backend.model.User;
import com.tccd.ecommerce_backend.repository.UserRepository;
import com.tccd.ecommerce_backend.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest)
    {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent())
        {
            return ResponseEntity.badRequest().body(null);
        }
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));
        user.setName(signupRequest.getName());
        user.setRole(Role.USER);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest)
    {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user == null)
        {
            return ResponseEntity.status(401).body(null);
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash()))
        {
            return ResponseEntity.status(401).body(null);
        }
        String token = jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
