package com.example.jeogiyoproject.domain.foodstore.dto.res;

import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class FoodStoreSearchResponseDto {
    private final Long id;
    private final String title;
    private final String address;
    private final Integer minPrice;
    private final LocalTime openAt;
    private final LocalTime closeAt;
//    private final List<MenuResponseDto> menus;

    public FoodStoreSearchResponseDto(
            Long id,
            String title,
            String address,
            Integer minPrice,
            LocalTime openAt,
            LocalTime closeAt
//            List<MenuResponseDto> menus
    ) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.minPrice = minPrice;
        this.openAt = openAt;
        this.closeAt = closeAt;
//        this.menus = menus;
    }
}
