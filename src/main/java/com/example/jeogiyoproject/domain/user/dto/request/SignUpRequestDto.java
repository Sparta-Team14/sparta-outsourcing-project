package com.example.jeogiyoproject.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email; // 아이디
    private String password; // 비밀번호
    private String name; // 이름
    private String address; // 주소
    private String role; // 역할
}
