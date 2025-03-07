package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.user.dto.response.KakaoLoginResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.impl.OAuthInfoResponse;
import com.example.jeogiyoproject.domain.user.entity.KakaoMember;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.KakaoRepository;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.auth.AuthTokens;
import com.example.jeogiyoproject.global.auth.OAuthLoginParams;
import com.example.jeogiyoproject.global.auth.OAuthProvider;
import com.example.jeogiyoproject.global.auth.RequestOAuthInfoService;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.security.AuthProvider;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RequestOAuthInfoService requestOAuthInfoService;
    private final KakaoRepository kakaoRepository;
    private final JwtUtil jwtUtil;

    public KakaoLoginResponseDto login(OAuthLoginParams params) throws ParseException {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        KakaoMember user = kakaoRepository.findByEmail(oAuthInfoResponse.getEmail()).orElse(null);
        if (user==null) {
            user = kakaoRepository.save(new KakaoMember(oAuthInfoResponse.getEmail(), oAuthInfoResponse.getNickname(), OAuthProvider.KAKAO));
        }
        String token = jwtUtil.createToken(user.getId(), oAuthInfoResponse.getEmail(), UserRole.USER);
        return new KakaoLoginResponseDto(token, oAuthInfoResponse.getNickname(), oAuthInfoResponse.getEmail());
    }
}
