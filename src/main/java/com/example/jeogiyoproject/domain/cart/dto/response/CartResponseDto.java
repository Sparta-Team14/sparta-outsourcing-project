package com.example.jeogiyoproject.domain.cart.dto.response;

import com.example.jeogiyoproject.domain.cart.entity.Cart;
import com.example.jeogiyoproject.domain.cart.entity.CartItems;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CartResponseDto {
    private final Long cartId;
    private final Long foodstoreId;
    private final List<CartItemsResponseDto> items;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public static CartResponseDto fromCartAndCartItems(Cart cart, List<CartItems> cartItems) {
        List<CartItemsResponseDto> items = new ArrayList<>();
        for (CartItems item : cartItems) {
            items.add(CartItemsResponseDto.builder()
                    .cartItemsId(item.getId())
                    .menuId(item.getMenu().getId())
                    .quantity(item.getQuantity())
                    .build()
            );
        }
        return CartResponseDto.builder()
                .cartId(cart.getId())
                .foodstoreId(cart.getFoodstore().getId())
                .items(items)
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
