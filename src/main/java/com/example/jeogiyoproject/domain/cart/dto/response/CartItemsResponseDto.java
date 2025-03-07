package com.example.jeogiyoproject.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemsResponseDto {
    private final Long cartItemsId;
    private final Long menuId;
    private final Integer quantity;
}
