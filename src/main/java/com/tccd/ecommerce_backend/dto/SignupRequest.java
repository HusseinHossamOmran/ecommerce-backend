package com.tccd.ecommerce_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest
{
    private String email;
    private String password;
    private String name;
}
