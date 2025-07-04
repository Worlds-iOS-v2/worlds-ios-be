package com.example.cultive.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignInRequest {
    private String email;
    private String password;
}