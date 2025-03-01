package com.example.jeogiyoproject.domain.order.dto.response;

import lombok.Getter;

@Getter
public class OrderMenuResponseDto {
    private final Long menuId;
    private final String menuName;
    private final Integer amount;
    private final Integer price;

    public OrderMenuResponseDto(Long menuId, String menuName, Integer amount, Integer price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.amount = amount;
        this.price = price;
    }
}
