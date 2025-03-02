package com.example.jeogiyoproject.domain.order.dto.response;

import lombok.Getter;

@Getter
public class OrderMenuResponseDto {
    private final Long menuId;
    private final String menuName;
    private final Integer quantity;
    private final Integer price;

    public OrderMenuResponseDto(Long menuId, String menuName, Integer quantity, Integer price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.price = price;
    }
}
