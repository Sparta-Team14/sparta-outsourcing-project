package com.example.jeogiyoproject.domain.account.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final String name;
    private final String adress;
    private final String role;

    public UserResponseDto(String name, String adress, String role) {
        this.name = name;
        this.adress = adress;
        this.role = role;
    }
}
