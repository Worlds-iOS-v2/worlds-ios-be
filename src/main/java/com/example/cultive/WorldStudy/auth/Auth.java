package com.example.cultive.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Auth {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String email;
    private String password;
}