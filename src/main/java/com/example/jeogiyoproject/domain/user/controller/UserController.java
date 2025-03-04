package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserUpdateResponseDto;
import com.example.jeogiyoproject.domain.userservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) { // 회원탈퇴
        userService.deleteUser(id);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long id) { // 회원 조회
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PatchMapping("/users/{id}/profiles")
    public ResponseEntity<UserUpdateResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateRequestDto userUpdateRequestDto) { // 비밀번호 및 주소 변경
        return ResponseEntity.ok(userService.update(id, userUpdateRequestDto));
    }

    @PatchMapping("/users/{id}/profiles/role")
    public ResponseEntity<RoleUpdateResponseDto> updateRole(@PathVariable Long id, @RequestBody RoleUpdateRequestDto roleUpdateRequestDto) { // 역할 변경
        return ResponseEntity.ok(userService.updateRole(id, roleUpdateRequestDto));
    }

}
