package com.example.jeogiyoproject.domain.account.controller;

import com.example.jeogiyoproject.domain.account.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.account.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
