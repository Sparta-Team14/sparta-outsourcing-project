package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.user.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponseDto save(SignUpRequestDto requestDto) { // 회원가입

        if (!requestDto.getRole().equals("OWNER") && !requestDto.getRole().equals("USER")) {
            throw new CustomException(ErrorCode.ROLE_IS_WRONG);
        }
        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getEmail(), password, requestDto.getName(), requestDto.getAddress(), requestDto.getRole());
        userRepository.save(user);

        return new SignUpResponseDto(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
