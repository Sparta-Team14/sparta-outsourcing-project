package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.request.LoginRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.LoginResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) { // 회원가입
        return ResponseEntity.ok(authService.save(signUpRequestDto));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) { // 로그인
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

//    @GetMapping("/auth/login/kakao")
//    public void kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {
//        authService.oAuthLogin(accessCode, httpServletResponse);
//    }
}
