package com.example.jeogiyoproject.domain.foodstore.dto.res;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class FoodStoreResponseDto {
    private final long id;
//    private final String ownerId;
    private final String title;
    private final String address;
    private final Integer minPrice;
    private final LocalDateTime openAt;
    private final LocalDateTime closeAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public FoodStoreResponseDto(
            long id,
//            String ownerId,
            String title,
            String address,
            Integer minPrice,
            LocalDateTime openAt,
            LocalDateTime closeAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
//        this.ownerId = ownerId;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
