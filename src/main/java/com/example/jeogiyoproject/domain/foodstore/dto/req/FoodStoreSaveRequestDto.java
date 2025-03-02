package com.example.jeogiyoproject.domain.foodstore.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class FoodStoreSaveRequestDto {
    private String title;
    private String address;
    private Integer minPrice;
    private LocalTime openAt;
    private LocalTime closeAt;
}
