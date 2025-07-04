package com.example.cultive.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthResponse {
    private String token;
    private String email;
}