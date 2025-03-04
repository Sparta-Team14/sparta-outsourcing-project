package com.example.jeogiyoproject.domain.account.dto.response;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final String barerToken;

    public LoginResponseDto(String barerToken) {
        this.barerToken = barerToken;
    }
}
