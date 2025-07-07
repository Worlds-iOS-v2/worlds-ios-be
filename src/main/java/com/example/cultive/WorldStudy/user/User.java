package com.example.cultive.WorldStudy.user;
import com.example.cultive.WorldStudy.user.enums.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


// src/main/java/com/example/cultive/WorldStudy/user/User.java

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role_grade")
    private UserRoleGrade userRoleGrade;

    @Column(name = "user_reported_count")
    private Long reportedCount;

    @Column(name = "user_point")
    private Long userPoint;

    @Enumerated(EnumType.STRING)
    @Column(name = "korean_grade")
    private KoreanGrade koreanGrade;

    @Column(name = "user_refresh_token")
    private String refreshToken;
}