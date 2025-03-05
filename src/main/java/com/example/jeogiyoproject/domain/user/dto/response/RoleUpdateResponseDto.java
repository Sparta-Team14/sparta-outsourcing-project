package com.example.jeogiyoproject.domain.user.dto.response;

import com.example.jeogiyoproject.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoleUpdateResponseDto {
    private final Long id;
    private final String email;
    private final UserRole userRole;

    public RoleUpdateResponseDto(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}
