package com.example.jeogiyoproject.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDto {
    private String password;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
    private String newPassword;
}
