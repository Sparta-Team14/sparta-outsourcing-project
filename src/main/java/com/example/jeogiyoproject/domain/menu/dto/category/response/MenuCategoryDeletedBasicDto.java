package com.example.jeogiyoproject.domain.menu.dto.category.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MenuCategoryDeletedBasicDto {
    private final Long categoryId;
    private final String name;
    private final LocalDateTime deletedAt;
}
