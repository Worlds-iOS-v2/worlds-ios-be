package com.example.cultive.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 유저 관련 DB 쿼리
}