package com.example.jeogiyoproject.domain.account.controller;

import com.example.jeogiyoproject.domain.account.dto.request.LoginRequestDto;
import com.example.jeogiyoproject.domain.account.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.account.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.account.service.AuthService;
import com.example.jeogiyoproject.domain.account.service.UserService;
import lombok.Getter;
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
