package com.example.jeogiyoproject.domain.user.enums;

import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;

import java.util.Arrays;

public enum UserRole {
    OWNER, USER, ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(userRole -> userRole.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ROLE_IS_WRONG));
    }
}
