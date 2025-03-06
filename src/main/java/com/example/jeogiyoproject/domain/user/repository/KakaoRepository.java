package com.example.jeogiyoproject.domain.user.repository;

import com.example.jeogiyoproject.domain.user.entity.KakaoMember;
import com.example.jeogiyoproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoRepository extends JpaRepository<KakaoMember, Long> {
    Optional<KakaoMember> findByEmail(String email);
}
