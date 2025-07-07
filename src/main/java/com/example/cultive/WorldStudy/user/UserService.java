package com.example.cultive.WorldStudy.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cultive.WorldStudy.common.ApiResponse;
import com.example.cultive.WorldStudy.user.dto.SignUpRequest;
import com.example.cultive.WorldStudy.user.dto.SignUpResponse;
import com.example.cultive.WorldStudy.user.enums.UserRole;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // 1. 회원가입
    public SignUpResponse signUp(SignUpRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        
        // 유효성 검사
        // validateSignUpRequest(request);
        
        // User 엔티티 생성
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setBirthDate(request.getBirthDate());
        user.setUserRole(request.getUserRole());
        user.setUserPoint(0L);
        user.setReportedCount(0L);
        user.setRefreshToken(null);
        
        // 역할별 추가 정보 설정
        if (request.getUserRole() == UserRole.MENTEE) {
            if (request.getKoreanGrade() == null) {
                throw new RuntimeException("MENTEE는 한국어 등급을 선택해야 합니다.");
            }
            user.setKoreanGrade(request.getKoreanGrade());
        } else if (request.getUserRole() == UserRole.MENTOR) {
            if (request.getSubjects() == 
            null || request.getSubjects().isEmpty()) {
                throw new RuntimeException("MENTOR는 최소 하나의 과목을 선택해야 합니다.");
            }
            // TODO: mentor_subjects 테이블에 과목 정보 저장 로직 추가
        }
        
        // 사용자 저장
        User savedUser = userRepository.save(user);
        
        return new SignUpResponse(savedUser.getId());
    }

    // 5. 로그아웃
    public void signOut(Long userId, String refreshToken) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new 
            RuntimeException("사용자를 찾을 수 없습니다."));
        
        // Refresh Token 삭제
        user.setRefreshToken(null);
        userRepository.save(user);
    }
    
    // 6. 회원탈퇴
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // TODO: 연관된 데이터 삭제 로직 추가 (질문, 답변, 채팅 등)
        
        userRepository.delete(user);
    }
    
    // 7. 유효성 검사
    private void validateSignUpRequest(SignUpRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("이메일은 필수입니다.");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("비밀번호는 필수입니다.");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("이름은 필수입니다.");
        }
        if (request.getBirthDate() == null) {
            throw new RuntimeException("생년월일은 필수입니다.");
        }
        if (request.getUserRole() == null) {
            throw new RuntimeException("사용자 역할은 필수입니다.");
        }
    }
    
    // 사용자 정보 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}