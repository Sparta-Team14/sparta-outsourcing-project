package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderHistoryResponseDto {
    private final Long orderId;
    private final String status;
    private final Long foodstoreId;
    private final String foodStoreTitle;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final String request;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;

    public static OrderHistoryResponseDto fromOrder(Order order) {
        return OrderHistoryResponseDto.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .foodstoreId(order.getFoodstore().getId())
                .foodStoreTitle(order.getFoodstore().getTitle())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .request(order.getRequest())
                .orderedAt(order.getCreatedAt())
                .build();
    }
}
