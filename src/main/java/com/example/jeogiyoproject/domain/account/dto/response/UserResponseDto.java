package com.example.jeogiyoproject.domain.account.dto.response;

import com.example.jeogiyoproject.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final String name;
    private final String adress;
    private final UserRole role;

    public UserResponseDto(String name, String adress, UserRole role) {
        this.name = name;
        this.adress = adress;
        this.role = role;
    }
}
