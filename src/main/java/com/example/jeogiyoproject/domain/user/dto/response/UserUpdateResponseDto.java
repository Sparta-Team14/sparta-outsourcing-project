package com.example.jeogiyoproject.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserUpdateResponseDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String address;

    public UserUpdateResponseDto(Long id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }
}
