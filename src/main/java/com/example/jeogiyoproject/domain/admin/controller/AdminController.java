package com.example.jeogiyoproject.domain.admin.controller;

import com.example.jeogiyoproject.domain.admin.dto.DailyOrderCountDto;
import com.example.jeogiyoproject.domain.admin.dto.DailyOrderTotalDto;
import com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderCountDto;
import com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderTotalDto;
import com.example.jeogiyoproject.domain.admin.service.AdminService;
import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.web.aop.annotation.AuthCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @AuthCheck(UserRole.ADMIN)
    @PatchMapping("/admin/users/{id}")
    public ResponseEntity<RoleUpdateResponseDto> changeUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequestDto roleUpdateRequestDto) { // 관리자인 사람이 권한 변경
        return ResponseEntity.ok(adminService.updateRole(id, roleUpdateRequestDto));
    }
    @AuthCheck(UserRole.ADMIN)
    @GetMapping("/admin/order-count/daily")
    public ResponseEntity<List<DailyOrderCountDto>> dailyOrderCount(@RequestParam int year,
                                                                    @RequestParam int month){
        return ResponseEntity.ok(adminService.dailyOrderCount(year,month));
    }
    @AuthCheck(UserRole.ADMIN)
    @GetMapping("/admin/order-count/monthly")
    public ResponseEntity<List<MonthlyOrderCountDto>> monthlyOrderCount(@RequestParam int year){
        return ResponseEntity.ok(adminService.monthlyOrderCount(year));
    }
    @AuthCheck(UserRole.ADMIN)
    @GetMapping("/admin/order-total/daily")
    public ResponseEntity<List<DailyOrderTotalDto>> dailyOrderTotal(@RequestParam int year,
                                                                    @RequestParam int month){
        return ResponseEntity.ok(adminService.dailyOrderTotal(year, month));
    }
    @AuthCheck(UserRole.ADMIN)
    @GetMapping("/admin/order-total/monthly")
    public ResponseEntity<List<MonthlyOrderTotalDto>> monthlyOrderTotal(@RequestParam int year){
        return ResponseEntity.ok(adminService.monthlyOrderTotal(year));
    }
}
