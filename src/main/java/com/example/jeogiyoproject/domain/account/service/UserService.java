package com.example.jeogiyoproject.domain.account.service;

import com.example.jeogiyoproject.domain.account.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.account.entity.User;
import com.example.jeogiyoproject.domain.account.repository.UserRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long id) { // 회원 탈퇴
        if(!userRepository.existsById(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDto findUser(Long id) { // 회원 조회
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        return new UserResponseDto(user.getName(), user.getAddress(), user.getRole());
    }
}
