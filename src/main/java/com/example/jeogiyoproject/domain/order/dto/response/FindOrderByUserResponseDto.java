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
public class FindOrderByUserResponseDto {
    private final Long orderId;
    private final Long foodstoreId;
    private final String foodstoreTitle;
    private final Integer totalPrice;
    private final Integer totalQuantity;
    private final String request;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime orderedAt;
    private final List<OrderMenuResponseDto> items;

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
                .foodstoreId(order.getFoodstore().getId())
                .foodstoreTitle(order.getFoodstore().getTitle())
                .totalPrice(order.getTotalPrice())
                .totalQuantity(order.getTotalQuantity())
                .request(order.getRequest())
                .orderedAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
