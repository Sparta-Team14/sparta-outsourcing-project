package com.example.jeogiyoproject.domain.order.service;

import com.example.jeogiyoproject.domain.order.dto.request.CreateOrderRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import com.example.jeogiyoproject.global.common.dto.AuthUser;

public interface OrderServiceInterface {
    CreateOrderResponseDto createOrder(AuthUser authUser, Long foodstoreId, CreateOrderRequestDto dto);
}
