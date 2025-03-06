package com.example.jeogiyoproject.domain.user.dto.response;

import com.example.jeogiyoproject.global.auth.OAuthProvider;
import lombok.Getter;

@Getter
public class KakaoLoginResponseDto {
    private final String barerToken;

    public KakaoLoginResponseDto(String barerToken) {
        this.barerToken = barerToken;
    }
}
