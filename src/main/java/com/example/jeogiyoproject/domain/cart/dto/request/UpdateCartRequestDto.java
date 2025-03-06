package com.example.jeogiyoproject.domain.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartRequestDto {
    private List<UpdateCartItemsRequestDto> items;
}
