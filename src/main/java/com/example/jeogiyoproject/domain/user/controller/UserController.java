package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserPasswordUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserAddressUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.service.UserService;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/users/delete")
    public void deleteUser(@RequestBody UserDeleteRequestDto userDeleteRequestDto, @Auth AuthUser authUser) { // 회원탈퇴
        userService.deleteUser(authUser.getId(), userDeleteRequestDto);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long id) { // 회원 조회
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PatchMapping("/users/profiles/address")
    public ResponseEntity<UserAddressUpdateResponseDto> updateAdress(@RequestBody UserAddressUpdateRequestDto userUpdateRequestDto, @Auth AuthUser authUser) { // 주소 변경
        return ResponseEntity.ok(userService.updateAdress(authUser.getId(), userUpdateRequestDto));
    }

    @PatchMapping("/users/profiles/password")
    public ResponseEntity<UserPasswordUpdateResponseDto> updatePassword(@RequestBody UserPasswordUpdateRequestDto userPasswordUpdateRequestDto, @Auth AuthUser authUser) { // 비밀번호 변경
        return ResponseEntity.ok(userService.updatePassword(authUser.getId(), userPasswordUpdateRequestDto));
    }

}
