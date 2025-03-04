package com.example.jeogiyoproject.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @Email
    private String email; // 아이디
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
    private String password; // 비밀번호
    private String name; // 이름
    private String address; // 주소
    private String role; // 역할
}
