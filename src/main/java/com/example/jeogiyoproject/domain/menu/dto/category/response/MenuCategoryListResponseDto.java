package com.example.jeogiyoproject.domain.menu.dto.category.response;

import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuBasicDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MenuCategoryListResponseDto {
    private final Long categoryId;
    private final String name;
    private final List<MenuBasicDto> menus;

    public MenuCategoryListResponseDto(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
        this.menus = new ArrayList<>();
    }

    public void addMenu(MenuBasicDto menu) {
        this.menus.add(menu);
    }
}
