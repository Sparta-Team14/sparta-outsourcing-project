package com.example.jeogiyoproject.domain.foodstore.dto.res;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class FoodStoreUpdateResponseDto {
    private final Long id;
    private final String title;
    private final String address;
    private final Integer minPrice;
    private final LocalTime openAt;
    private final LocalTime closeAt;
    private final LocalDateTime updatedAt;

    public FoodStoreUpdateResponseDto(Long id, String title, String address, Integer minPrice, LocalTime openAt, LocalTime closeAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.updatedAt = updatedAt;
    }
}
