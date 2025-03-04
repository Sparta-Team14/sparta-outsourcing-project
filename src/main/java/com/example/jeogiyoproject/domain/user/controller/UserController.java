package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserPasswordUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserAddressUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id, @RequestBody UserDeleteRequestDto userDeleteRequestDto) { // 회원탈퇴
        userService.deleteUser(id, userDeleteRequestDto);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long id) { // 회원 조회
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PatchMapping("/users/{id}/profiles")
    public ResponseEntity<UserAddressUpdateResponseDto> update(@PathVariable Long id, @RequestBody UserAddressUpdateRequestDto userUpdateRequestDto) { // 주소 변경
        return ResponseEntity.ok(userService.update(id, userUpdateRequestDto));
    }

//    @PatchMapping("/users/{id}/profiles")
//    public ResponseEntity<UserPasswordUpdateResponseDto> updatePassword(@PathVariable Long id, @RequestBody UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
//        return ResponseEntity.ok(userService.updatePassword(id, userPasswordUpdateRequestDto));
//    }

}
