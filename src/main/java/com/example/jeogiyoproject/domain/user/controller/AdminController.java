package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.service.UserAdminService;
import com.example.jeogiyoproject.global.common.annotation.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final UserAdminService adminService;

    @Admin
    @PatchMapping("/admin/users/{id}")
    public ResponseEntity<RoleUpdateResponseDto> changeUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequestDto roleUpdateRequestDto) { // 관리자인 사람이 권한 변경
        return ResponseEntity.ok(adminService.updateRole(id, roleUpdateRequestDto));
    }
}
