package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindOrdersResponseDto {
    private final Long orderId;
    private final String status;
    private final Integer totalPrice;
    private final String request;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;

    private FindOrdersResponseDto(Long orderId, String status, Integer totalPrice, String request, LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.request = request;
        this.orderedAt = orderedAt;
    }

    public static FindOrdersResponseDto fromOrder(Order order) {
        return new FindOrdersResponseDto(order.getId(),
                order.getStatus().name(),
                order.getTotalPrice(),
                order.getRequest(),
                order.getCreatedAt());
    }
}
