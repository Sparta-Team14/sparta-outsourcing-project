package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FindOrderByUserResponseDto {
    private final Long orderId;
    private final Long foodstoreId;
    private final String foodstoreTitle;
    private final Integer totalPrice;
    private final Integer count;
    private final String request;
    private final List<OrderMenuResponseDto> items;

    public FindOrderByUserResponseDto(Long orderId, Long foodstoreId, String foodstoreTitle, Integer totalPrice, Integer count, String request, List<OrderMenuResponseDto> items) {
        this.orderId = orderId;
        this.foodstoreId = foodstoreId;
        this.foodstoreTitle = foodstoreTitle;
        this.totalPrice = totalPrice;
        this.count = count;
        this.request = request;
        this.items = items;
    }

    public static FindOrderByUserResponseDto fromOrderAndOrderDetail(Order order, List<OrderDetail> orderDetails) {
        List<OrderMenuResponseDto> items = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            new OrderMenuResponseDto(
                    orderDetail.getMenu().getId(),
                    orderDetail.getMenu().getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getQuantity() * orderDetail.getMenu().getPrice()
            );
        }
        return new FindOrderByUserResponseDto(
                order.getId(),
                order.getFoodStore().getId(),
                order.getFoodStore().getTitle(),
                order.getTotalPrice(),
                1,
                order.getRequest(),
                items
        );
    }
}
