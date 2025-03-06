package com.example.jeogiyoproject.domain.admin.service;

import com.example.jeogiyoproject.domain.admin.dto.DailyOrderCountDto;
import com.example.jeogiyoproject.domain.admin.dto.DailyOrderTotalDto;
import com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderCountDto;
import com.example.jeogiyoproject.domain.admin.dto.MonthlyOrderTotalDto;
import com.example.jeogiyoproject.domain.order.repository.OrderRepository;
import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public RoleUpdateResponseDto updateRole(Long id, RoleUpdateRequestDto roleUpdateRequestDto) { // 회원 역할 수정
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_IS_NOT_EXIST)
        );
        UserRole userRole = UserRole.of(roleUpdateRequestDto.getRole());
        user.updateRole(userRole);

        return new RoleUpdateResponseDto(user.getId(), user.getEmail(), user.getRole());
    }
    @Transactional(readOnly = true)
    public List<DailyOrderCountDto> dailyOrderCount(int year, int month) {
        return orderRepository.findDailyOrderCountsByYearAndMonth(year, month);
    }
    @Transactional(readOnly = true)
    public List<DailyOrderTotalDto> dailyOrderTotal(int year, int month) {
        return orderRepository.findDailyOrderTotalByYearAndMonth(year, month);
    }
    @Transactional(readOnly = true)
    public List<MonthlyOrderCountDto> monthlyOrderCount(int year) {
        return orderRepository.findMonthlyOrderCountByYear(year);
    }
    @Transactional(readOnly = true)
    public List<MonthlyOrderTotalDto> monthlyOrderTotal(int year) {
        return orderRepository.findMonthlyOrderTotalByYear(year);
    }
}
