package com.example.jeogiyoproject.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderMenuResponseDto {
    private final Long menuId;
    private final String menuName;
    private final Integer quantity;
    private final Integer price;
}
