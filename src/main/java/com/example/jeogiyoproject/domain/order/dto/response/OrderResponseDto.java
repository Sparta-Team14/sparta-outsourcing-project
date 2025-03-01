package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResponseDto {
    private final Long orderId;
    private final String address;
    private final Integer totalPrice;
    private final List<OrderMenuResponseDto> menus;

    private OrderResponseDto(Long orderId, String address, Integer totalPrice, List<OrderMenuResponseDto> menus) {
        this.orderId = orderId;
        this.address = address;
        this.totalPrice = totalPrice;
        this.menus = menus;
    }

    public static OrderResponseDto fromOrderAndOrderDetail(Order order, List<OrderDetail> orderDetails) {
        List<OrderMenuResponseDto> menus = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            new OrderMenuResponseDto(
                    orderDetail.getMenu().getId(),
                    orderDetail.getMenu().getName(),
                    orderDetail.getAmount(),
                    orderDetail.getAmount() * orderDetail.getMenu().getPrice()
            );
        }
        return new OrderResponseDto(
                order.getId(),
                order.getUser().getAddress(),
                order.getTotalPrice(),
                menus
        );
    }
}


