package com.tccd.ecommerce_backend.controller;

import com.tccd.ecommerce_backend.dto.UpdateUserRequest;
import com.tccd.ecommerce_backend.dto.UserResponse;
import com.tccd.ecommerce_backend.model.User;
import com.tccd.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
public class UserController
{
    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public ResponseEntity<UserResponse> getUser()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole().name());
        return ResponseEntity.ok(response);
    }
    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if(updateUserRequest.getName() != null)
        {
            user.setName(updateUserRequest.getName());
        }
        userRepository.save(user);
        UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole().name());
        return ResponseEntity.ok(response);
    }

}
