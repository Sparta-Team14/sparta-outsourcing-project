package com.example.jeogiyoproject.domain.foodstore.dto.res;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class FoodStoreSearchResponseDto {
    private final Long id;
    private final String title;
    private final String address;
    private final Integer minPrice;
    private final LocalTime openAt;
    private final LocalTime closeAt;

    public FoodStoreSearchResponseDto(
            Long id,
            String title,
            String address,
            Integer minPrice,
            LocalTime openAt,
            LocalTime closeAt
    ) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }
}
