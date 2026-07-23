package com.tccd.ecommerce_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse
{
    private Long id;
    private String email;
    private String name;
    private String role;
}
