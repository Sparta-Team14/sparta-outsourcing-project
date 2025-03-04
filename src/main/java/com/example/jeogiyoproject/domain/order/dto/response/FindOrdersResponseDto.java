package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindOrdersResponseDto {
    private final Long orderId;
    private final String status;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final String request;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;

    public static FindOrdersResponseDto fromOrder(Order order) {
        return FindOrdersResponseDto.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .request(order.getRequest())
                .orderedAt(order.getCreatedAt())
                .build();
    }
}
