package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChangeOrderStatusResponseDto {
    private final Long orderId;
    private final Long foodstoreId;
    private final String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public static ChangeOrderStatusResponseDto fromOrder(Order order) {
        return ChangeOrderStatusResponseDto.builder()
                .orderId(order.getId())
                .foodstoreId(order.getFoodstore().getId())
                .status(order.getStatus().name())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
