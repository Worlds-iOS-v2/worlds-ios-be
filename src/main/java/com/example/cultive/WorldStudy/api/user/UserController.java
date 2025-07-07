package com.example.cultive.WorldStudy.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.cultive.WorldStudy.auth.CustomUserDetails;
import com.example.cultive.WorldStudy.auth.JwtTokenProvider;
import com.example.cultive.WorldStudy.auth.dto.TokenRequest;
import com.example.cultive.WorldStudy.common.ApiResponse;
import com.example.cultive.WorldStudy.user.*;
import com.example.cultive.WorldStudy.user.dto.SignUpRequest;
import com.example.cultive.WorldStudy.user.dto.SignUpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signup(@RequestBody SignUpRequest request) {
        try {
            SignUpResponse signUpResponse = userService.signUp(request);
            ApiResponse<SignUpResponse> response = new ApiResponse<>("회원가입 성공", 200, signUpResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<SignUpResponse> errorResponse = new ApiResponse<>(e.getMessage(), 400, null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<String> signOut(@RequestBody TokenRequest request) {
        String refreshToken = request.getRefreshToken();
        try {
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            userService.signOut(userId, refreshToken);
            return ResponseEntity.ok("로그아웃 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // 회원탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("회원탈퇴 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}