package com.example.jeogiyoproject.domain.user.dto.request;

import com.example.jeogiyoproject.global.auth.OAuthProvider;
import lombok.Getter;

@Getter
public class KakaoLoginRequestDto {
    private String email;
    private String nickName;
    private OAuthProvider oAuthProvider;
}
