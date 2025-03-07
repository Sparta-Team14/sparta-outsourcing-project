package com.example.jeogiyoproject.domain.user.dto.response;

import com.example.jeogiyoproject.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final UserRole userRole;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public SignUpResponseDto(Long id, String name, String email, UserRole userRole, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
