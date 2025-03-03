package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.request.LoginRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) { // 회원가입
        return ResponseEntity.ok(authService.save(signUpRequestDto));
    }

    @PostMapping("/auth/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto) { // 로그인
    }
}
