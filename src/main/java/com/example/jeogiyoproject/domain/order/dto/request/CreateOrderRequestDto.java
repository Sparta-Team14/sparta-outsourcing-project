package com.example.jeogiyoproject.domain.order.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequestDto {

    @Column(name = "주문메뉴", nullable = false)
    @NotNull(message = "주문메뉴는 필수값입니다.")
    private List<OrderMenuRequestDto> menus;
    @Column(name = "요청사항")
    private String request;
}
