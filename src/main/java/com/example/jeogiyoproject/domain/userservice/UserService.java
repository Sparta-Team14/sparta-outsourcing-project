package com.example.jeogiyoproject.domain.userservice;

import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.entity.Users;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void deleteUser(Long id) { // 회원 탈퇴
        if(!userRepository.existsById(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDto findUser(Long id) { // 회원 조회
        Users user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        return new UserResponseDto(user.getName(), user.getAddress(), user.getRole());
    }

    @Transactional
    public UserUpdateResponseDto update(Long id, UserUpdateRequestDto userUpdateRequestDto) { // 비밀번호 및 주소 변경
        Users user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        if (!passwordEncoder.matches(userUpdateRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }
        user.update(userUpdateRequestDto.getNewPassword(), userUpdateRequestDto.getAddress());
        return new UserUpdateResponseDto(user.getId(), user.getName(), user.getEmail(), user.getAddress());
    }

    @Transactional
    public RoleUpdateResponseDto updateRole(Long id, RoleUpdateRequestDto roleUpdateRequestDto) { // 회원 역할 수정
        Users user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        if (!passwordEncoder.matches(roleUpdateRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }
        UserRole userRole = UserRole.of(roleUpdateRequestDto.getRole());
        user.updaterole(userRole);

        return new RoleUpdateResponseDto(user.getId(), user.getEmail(), userRole, user.getUpdatedAt());
    }
}
