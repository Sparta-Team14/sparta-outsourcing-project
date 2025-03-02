package com.example.jeogiyoproject.domain.menu.dto.menu;

import com.example.jeogiyoproject.domain.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuResponseDto {
    private Long categoryId;
    private Long menuId;
    private String name;
    private String info;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static MenuResponseDto fromMenu(Menu menu){
        return MenuResponseDto.builder()
                .categoryId(menu.getMenuCategory().getId())
                .menuId(menu.getId())
                .name(menu.getName())
                .info(menu.getInfo())
                .price(menu.getPrice())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .deletedAt(menu.getDeletedAt())
                .build();
    }
}
