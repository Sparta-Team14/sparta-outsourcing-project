package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
        return OrderHistoryResponseDto.builder()
                .orderId(order.getId())
                .foodstoreId(order.getFoodStore().getId())
                .foodStoreTitle(order.getFoodStore().getTitle())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .request(order.getRequest())
                .build();
    }
}
