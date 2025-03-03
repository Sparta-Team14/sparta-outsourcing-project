package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
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
            new OrderMenuResponseDto(
                    orderDetail.getMenu().getId(),
                    orderDetail.getMenu().getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getQuantity() * orderDetail.getMenu().getPrice()
            );
        }
        return new FindOrderResponseDto(
                order.getId(),
                order.getUser().getAddress(),
                order.getTotalPrice(),
                order.getTotalQuantity(),
                items
        );
    }
}


