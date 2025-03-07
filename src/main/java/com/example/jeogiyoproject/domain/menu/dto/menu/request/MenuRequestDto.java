package com.example.jeogiyoproject.domain.menu.dto.menu.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuRequestDto {
    private final Long categoryId;
    private final String name;
    private final String info;
    private final Integer price;
}