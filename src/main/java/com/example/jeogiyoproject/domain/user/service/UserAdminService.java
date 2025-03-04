package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RoleUpdateResponseDto updateRole(Long id, RoleUpdateRequestDto roleUpdateRequestDto) { // 회원 역할 수정
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        UserRole userRole = UserRole.of(roleUpdateRequestDto.getRole());
        user.updateRole(userRole);

        return new RoleUpdateResponseDto(user.getId(), user.getEmail(), user.getRole(), user.getUpdatedAt());
    }

}
