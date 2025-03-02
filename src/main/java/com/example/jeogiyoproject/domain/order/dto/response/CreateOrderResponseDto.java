package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateOrderResponseDto {
    private final Long orderId;
    private final String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;

    private CreateOrderResponseDto(Long orderId, String status, LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    public static CreateOrderResponseDto fromOrder(Order order) {
        return new CreateOrderResponseDto(order.getId(), order.getStatus().name(), order.getCreatedAt());
    }
}
