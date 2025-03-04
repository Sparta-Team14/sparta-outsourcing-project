package com.example.jeogiyoproject.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String password;
    private String newPassword;
    private String address;
}
