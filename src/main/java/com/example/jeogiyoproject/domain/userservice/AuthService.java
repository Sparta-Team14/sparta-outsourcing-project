package com.example.jeogiyoproject.domain.userservice;

import com.example.jeogiyoproject.domain.user.dto.request.LoginRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.LoginResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.user.entity.Users;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import com.example.jeogiyoproject.global.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponseDto save(SignUpRequestDto requestDto) { // 회원가입

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_IS_EXIST);
        }

        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRole userRole = UserRole.valueOf(requestDto.getRole());
        Users user = new Users(requestDto.getEmail(), password, requestDto.getName(), requestDto.getAddress(), userRole);
        userRepository.save(user);

        return new SignUpResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Users user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());

        return new LoginResponseDto(bearerToken);
    }
}
