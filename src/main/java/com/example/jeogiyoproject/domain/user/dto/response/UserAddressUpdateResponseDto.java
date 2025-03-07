package com.example.jeogiyoproject.domain.user.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserAddressUpdateResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String address;
    private final LocalDateTime updatedAt;

    public UserAddressUpdateResponseDto(Long id, String name, String email, String address, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.updatedAt = updatedAt;
    }
}
