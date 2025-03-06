package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.response.KakaoLoginResponseDto;
import com.example.jeogiyoproject.domain.user.service.KakaoService;
import com.example.jeogiyoproject.global.auth.KakaoLoginParams;
import com.example.jeogiyoproject.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kaKaoService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/login/kakao")
    public KakaoLoginResponseDto kakaoLogin(@RequestBody KakaoLoginParams kakaoLoginParams) throws ParseException {
       return kaKaoService.login(kakaoLoginParams);
    }
}
