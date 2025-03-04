package com.example.jeogiyoproject.global.util;

import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretkey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // HS256으로 초기화

    @PostConstruct
    public void init() { // 기본 생성
        byte[] bytes = Base64.getDecoder().decode(secretkey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Long userId, String email, UserRole userRole) { // 토큰 생성
        Date date = new Date();

        return BEARER_PREFIX + // Bearer
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) // userid 추가
                        .claim("email", email) // 이메일 추가
                        .claim("userRole", userRole) // 역할 추가
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //유효 시간
                        .setIssuedAt(date) //만료시간
                        .signWith(key, signatureAlgorithm) // hs256으로 서명한다.
                        .compact(); // builder
    }

    public String substringToken(String tokenValue) { // Barers 앞 7글자를 자름
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ErrorCode.TOKEN_IS_NOT_FOUND);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
