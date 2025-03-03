package com.example.jeogiyoproject.domain.menu.dto.category.response;


import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuCategoryResponseDto {
    private Long id;
    private Long foodstoreId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static MenuCategoryResponseDto fromMenuCategory(MenuCategory category){
        return MenuCategoryResponseDto.builder()
                .id(category.getId())
                .foodstoreId(category.getFoodStore().getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .deletedAt(category.getDeletedAt())
                .build();
    }

}
