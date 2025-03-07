package com.example.jeogiyoproject.domain.user.entity;

import com.example.jeogiyoproject.domain.user.controller.KakaoController;
import com.example.jeogiyoproject.global.auth.OAuthProvider;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class KakaoMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String email;
    private String nickname;
    private OAuthProvider oAuthProvider;

    @Builder
    public KakaoMember(String email, String nickname, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.oAuthProvider = oAuthProvider;
    }
}
