package com.example.jeogiyoproject.domain.user.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserPasswordUpdateResponseDto {
    private final Long id;
    private final String email;
    private final String name;
    private final String address;
    private final LocalDateTime updatedAt;

    public UserPasswordUpdateResponseDto(Long id, String email, String name, String address, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.updatedAt = updatedAt;
    }
}
