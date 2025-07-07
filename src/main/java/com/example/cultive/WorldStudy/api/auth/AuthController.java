package com.example.cultive.WorldStudy.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cultive.WorldStudy.auth.AuthService;
import com.example.cultive.WorldStudy.auth.JwtTokenProvider;
import com.example.cultive.WorldStudy.auth.dto.SignInRequest;
import com.example.cultive.WorldStudy.auth.dto.TokenRequest;
import com.example.cultive.WorldStudy.auth.dto.TokenResponse;
import com.example.cultive.WorldStudy.auth.dto.UserInfoResponse;
import com.example.cultive.WorldStudy.user.User;
import com.example.cultive.WorldStudy.user.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(JwtTokenProvider jwtTokenProvider, UserService userService, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
        this.userService = userService;
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            TokenResponse response = authService.signIn(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    // Refresh Token으로 새로운 Access Token 발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest request) {
        try {
            TokenResponse response = authService.refreshAccessToken(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            // 만료 관련 메시지라면 401, 그 외는 400
            if (e.getMessage().toLowerCase().contains("expired") || e.getMessage().toLowerCase().contains("토큰 만료")) {
                return ResponseEntity.status(401).body(errorResponse);
            }
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // 사용자 정보 조회
    @PostMapping("/userinfo")
    public ResponseEntity<UserInfoResponse> getUserInfoByRefreshToken(@RequestBody TokenRequest request) {
        String refreshToken = request.getRefreshToken();
        try {
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            User user = userService.getUserById(userId);
            UserInfoResponse response = new UserInfoResponse(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}