package com.example.cultive.WorldStudy.auth.dto;

import java.time.LocalDate;

import com.example.cultive.WorldStudy.user.User;
import com.example.cultive.WorldStudy.user.enums.KoreanGrade;
import com.example.cultive.WorldStudy.user.enums.UserRole;
import com.example.cultive.WorldStudy.user.enums.UserRoleGrade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
        private Long id;
        private String email;
        private String name;
        private LocalDate birthDate;
        private UserRole userRole;
        private UserRoleGrade userRoleGrade;
        private Long reportedCount;
        private Long userPoint;
        private KoreanGrade koreanGrade;

        public UserInfoResponse(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.name = user.getName();
            this.birthDate = user.getBirthDate();
            this.userRole = user.getUserRole();
            this.userRoleGrade = user.getUserRoleGrade();
            this.reportedCount = user.getReportedCount();
            this.userPoint = user.getUserPoint();
            this.koreanGrade = user.getKoreanGrade();
        }
}
