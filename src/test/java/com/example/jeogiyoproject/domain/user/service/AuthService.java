package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.user.dto.request.SignUpRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.SignUpResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AuthService {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원가입을_해보자() {
        String email = "test@gmail.com";
        String password = "1234";
        String name = "test";
        String address = "seoul";
        UserRole userRole = UserRole.USER;

        SignUpRequestDto requestDto = new SignUpRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", email);
        ReflectionTestUtils.setField(requestDto, "password", password);
        ReflectionTestUtils.setField(requestDto, "name", name);
        ReflectionTestUtils.setField(requestDto, "address", address);

        User user = new User(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName(), requestDto.getAddress(), userRole);

//        BDDMockito.given(userRepository.existsByEmail(anyString())).willReturn(true);
//        BDDMockito.given(passwordEncoder.encode(anyString())).willReturn(password);

        SignUpResponseDto signUpResponseDto = new SignUpResponseDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());

        assertThat(signUpResponseDto.getName()).isEqualTo(name);
        assertThat(signUpResponseDto.getEmail()).isEqualTo(email);
    }
}
