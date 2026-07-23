package com.tccd.ecommerce_backend.service;

import com.tccd.ecommerce_backend.model.User;
import com.tccd.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException
    {
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPasswordHash(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

}
