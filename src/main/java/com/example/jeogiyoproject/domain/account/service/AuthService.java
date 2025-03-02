package com.example.jeogiyoproject.domain.account.service;

import com.example.jeogiyoproject.domain.account.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.account.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.account.entity.User;
import com.example.jeogiyoproject.domain.account.repository.UserRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public SignUpResponseDto save(SignUpRequestDto requestDto) { // 회원가입

        if (!requestDto.getRole().equals("OWNER") && !requestDto.getRole().equals("USER")) {
            throw new CustomException(ErrorCode.ROLE_IS_WRONG);
        }
        User user = new User(requestDto.getEmail(),requestDto.getPassword(), requestDto.getName(), requestDto.getAddress(), requestDto.getRole());
        userRepository.save(user);

        return new SignUpResponseDto(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
