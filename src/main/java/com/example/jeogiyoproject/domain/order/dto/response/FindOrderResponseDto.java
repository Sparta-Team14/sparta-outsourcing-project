package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FindOrderResponseDto {
    private final Long orderId;
    private final String address;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;
    private final List<OrderMenuResponseDto> items;

    public static FindOrderResponseDto fromOrderAndOrderDetail(Order order, List<OrderDetail> orderDetails) {
        List<OrderMenuResponseDto> items = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            items.add(OrderMenuResponseDto.builder()
                    .menuId(orderDetail.getMenu().getId())
                    .menuName(orderDetail.getMenu().getName())
                    .quantity(orderDetail.getQuantity())
                    .price(orderDetail.getQuantity() * orderDetail.getMenu().getPrice())
                    .build());
        }
        return FindOrderResponseDto.builder()
                .orderId(order.getId())
                .address(order.getUser().getAddress())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .orderedAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}


