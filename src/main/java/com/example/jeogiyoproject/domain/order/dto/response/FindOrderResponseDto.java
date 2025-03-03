package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FindOrderResponseDto {
    private final Long orderId;
    private final String address;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final List<OrderMenuResponseDto> items;

    private FindOrderResponseDto(Long orderId, String address, Integer totalPrice, Integer totalQuantity, List<OrderMenuResponseDto> items) {
        this.orderId = orderId;
        this.address = address;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.items = items;
    }

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
                .items(items)
                .build();
    }
}


