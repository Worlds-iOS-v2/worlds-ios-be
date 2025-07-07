package com.example.cultive.WorldStudy.user.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.cultive.WorldStudy.user.enums.UserRole;

import com.example.cultive.WorldStudy.user.enums.KoreanGrade;
import com.example.cultive.WorldStudy.user.enums.SubjectCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
    private LocalDate birthDate;
    private UserRole userRole;
    
    // MENTEE
    private KoreanGrade koreanGrade;
    
    // MENTOR
    private List<SubjectCategory> subjects;
}