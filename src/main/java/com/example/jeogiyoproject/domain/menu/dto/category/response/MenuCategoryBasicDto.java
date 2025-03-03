package com.example.jeogiyoproject.domain.menu.dto.category.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuCategoryBasicDto {
    private final Long categoryId;
    private final String name;

}
