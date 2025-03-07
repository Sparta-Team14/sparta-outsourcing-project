package com.example.jeogiyoproject.domain.user.dto.response;

import com.example.jeogiyoproject.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String name;
    private final String adress;
    private final UserRole role;

    public UserResponseDto(Long id, String name, String adress, UserRole role) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.role = role;
    }
}
