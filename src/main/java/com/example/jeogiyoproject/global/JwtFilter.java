package com.example.jeogiyoproject.global;

import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (url.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String baererJwt = request.getHeader("Authorization");

        if (baererJwt == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다!");
            return;
        }

        String jwt = jwtUtil.substringToken(baererJwt);

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 토큰입니다");
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));

            chain.doFilter(request, response);
        } catch (ServletException | MalformedInputException e) {
            log.error("유효하지 않는 JWT 서명입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지않는 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT Token입니다", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지않는 JWT 토큰입니다");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원하지않는 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("유효하지않는 JWT 토큰입니다");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않는 JWT 토큰입니다.");
        }

    }
}
