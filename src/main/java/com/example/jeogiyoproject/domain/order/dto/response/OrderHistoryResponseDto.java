package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderHistoryResponseDto {
    private final Long orderId;
    private final Long foodstoreId;
    private final String foodStoreTitle;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final String request;

    private OrderHistoryResponseDto(Long orderId, Long foodstoreId, String foodStoreTitle, Integer totalPrice, Integer totalQuantity, String request) {
        this.orderId = orderId;
        this.foodstoreId = foodstoreId;
        this.foodStoreTitle = foodStoreTitle;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.request = request;
    }

    public static OrderHistoryResponseDto fromOrder(Order order) {
        return new OrderHistoryResponseDto(order.getId(),
                order.getFoodStore().getId(),
                order.getFoodStore().getTitle(),
                order.getTotalPrice(),
                order.getTotalQuantity(),
                order.getRequest()
        );
    }
}
