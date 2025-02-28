package com.example.jeogiyoproject.domain.foodstore.dto.req;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FoodStoreSaveRequestDto {
    private String title;
    private String address;
    private Integer minPrice;
    private LocalDate openAt;
    private LocalDate closeAt;
}
