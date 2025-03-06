package com.example.jeogiyoproject.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemsRequestDto {
    @NotNull(message = "메뉴번호는 필수값입니다.")
    private Long menuId;
    @Min(value = 1L, message = "주문수량은 최소 1개 이상입니다.")
    private Integer quantity;
}
