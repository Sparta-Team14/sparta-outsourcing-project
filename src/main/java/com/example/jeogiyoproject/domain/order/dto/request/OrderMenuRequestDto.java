package com.example.jeogiyoproject.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuRequestDto {

    private Long menuId;
    private Integer quantity;
}
