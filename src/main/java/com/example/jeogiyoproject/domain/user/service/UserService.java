package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserPasswordUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserAddressUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void deleteUser(Long id, UserDeleteRequestDto userDeleteRequestDto) { // 회원 탈퇴
        User users = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        if (!passwordEncoder.matches(userDeleteRequestDto.getPassword(), users.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }

        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUser(Long id) { // 회원 조회
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        return new UserResponseDto(user.getId(), user.getName(), user.getAddress(), user.getRole());
    }

    @Transactional
    public UserAddressUpdateResponseDto update(Long id, UserAddressUpdateRequestDto userUpdateRequestDto) { // 주소 변경
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        if (!passwordEncoder.matches(userUpdateRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }
        user.update(userUpdateRequestDto.getAddress()); // 주소만 업데이트 가능하게 추가
        return new UserAddressUpdateResponseDto(user.getId(), user.getName(), user.getEmail(), user.getAddress(), user.getUpdatedAt());
    }

    @Transactional
    public UserPasswordUpdateResponseDto updatePassword(Long id, UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) { // 비밀번호 변경
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        if (!passwordEncoder.matches(userPasswordUpdateRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }
        user.updatePassword(userPasswordUpdateRequestDto.getNewPassword());

        return new UserPasswordUpdateResponseDto(user.getId(), user.getEmail(), user.getName(), user.getAddress(), user.getUpdatedAt());
    }


}
