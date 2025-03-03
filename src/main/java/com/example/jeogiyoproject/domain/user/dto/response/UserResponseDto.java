package com.example.jeogiyoproject.domain.user.dto.response;

import lombok.Getter;

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
