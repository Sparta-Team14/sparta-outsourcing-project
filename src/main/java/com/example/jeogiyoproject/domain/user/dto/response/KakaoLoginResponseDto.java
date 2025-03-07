package com.example.jeogiyoproject.domain.user.dto.response;

import com.example.jeogiyoproject.global.auth.OAuthProvider;
import lombok.Getter;

@Getter
public class KakaoLoginResponseDto {
    private final String barerToken;
    private final String nickname;
    private final String email;

    public KakaoLoginResponseDto(String barerToken, String nickname, String email) {
        this.barerToken = barerToken;
        this.nickname = nickname;
        this.email = email;
    }
}
