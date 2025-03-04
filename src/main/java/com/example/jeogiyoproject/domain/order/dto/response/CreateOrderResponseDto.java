package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
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
        return CreateOrderResponseDto.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .orderedAt(order.getCreatedAt())
                .build();
    }
}
