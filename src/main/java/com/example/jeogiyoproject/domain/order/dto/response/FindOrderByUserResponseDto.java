package com.example.jeogiyoproject.domain.order.dto.response;

import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FindOrderByUserResponseDto {
    private final Long orderId;
    private final Long foodstoreId;
    private final String foodstoreTitle;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final String request;
    private final List<OrderMenuResponseDto> items;

    public FindOrderByUserResponseDto(Long orderId, Long foodstoreId, String foodstoreTitle, Integer totalPrice, Integer totalQuantity, String request, List<OrderMenuResponseDto> items) {
        this.orderId = orderId;
        this.foodstoreId = foodstoreId;
        this.foodstoreTitle = foodstoreTitle;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.request = request;
        this.items = items;
    }

    public static FindOrderByUserResponseDto fromOrderAndOrderDetail(Order order, List<OrderDetail> orderDetails) {
        List<OrderMenuResponseDto> items = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            items.add(OrderMenuResponseDto.builder()
                    .menuId(orderDetail.getMenu().getId())
                    .menuName(orderDetail.getMenu().getName())
                    .quantity(orderDetail.getQuantity())
                    .price(orderDetail.getQuantity() * orderDetail.getMenu().getPrice())
                    .build());
        }
        return FindOrderByUserResponseDto.builder()
                .orderId(order.getId())
                .foodstoreId(order.getFoodStore().getId())
                .foodstoreTitle(order.getFoodStore().getTitle())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .request(order.getRequest())
                .items(items)
                .build();
    }
}
