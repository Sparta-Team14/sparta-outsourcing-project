package com.example.jeogiyoproject.domain.foodstore.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class FoodStoreRequestDto {
    private String title;
    private String address;
    private Integer minPrice;
    private LocalTime openAt;
    private LocalTime closeAt;
    private MultipartFile image;
}
