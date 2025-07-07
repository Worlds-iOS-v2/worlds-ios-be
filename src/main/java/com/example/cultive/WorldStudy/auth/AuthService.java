package com.example.cultive.WorldStudy.auth;

import com.example.cultive.WorldStudy.user.UserService;
import com.example.cultive.WorldStudy.user.User;
import com.example.cultive.WorldStudy.user.UserRepository;
import com.example.cultive.WorldStudy.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    public TokenResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        
        return new TokenResponse(accessToken, refreshToken);
    }

    // refresh token으로 access token 재발급
    public TokenResponse refreshAccessToken(TokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token is required");
        }
    
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // // 2. DB에 저장된 리프레시 토큰과 비교 (토큰 탈취 방지)
        // if (!refreshTokenService.existsValidToken(refreshToken)) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found or expired");
        // }

        // 3. 리프레시 토큰에서 userId 추출
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // 4. 유저 존재 여부 체크
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 5. 새로운 액세스 토큰 생성
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);

        // 6. (선택) 리프레시 토큰도 새로 발급 & 저장
        // String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
        // refreshTokenService.save(newRefreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        // tokens.put("refreshToken", newRefreshToken); // 필요하면

        return new TokenResponse(newAccessToken, refreshToken);
    }

}