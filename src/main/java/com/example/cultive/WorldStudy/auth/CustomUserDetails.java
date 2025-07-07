package com.example.cultive.WorldStudy.auth;

import com.example.cultive.WorldStudy.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 권한 반환 (간단 예시, 필요시 roles/authorities 변환)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 예시: 권한이 하나만 있을 때
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash(); // User 엔티티의 비밀번호 필드명에 맞게!
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 로그인에 사용하는 필드
    }

    // 계정 만료/잠김/활성화 등 (필요에 따라 true/false 반환)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 추가로 userId 등 커스텀 정보 꺼내고 싶으면 getter 추가
    public Long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
