package com.example.jeogiyoproject.domain.menu.dto.menu.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuUpdateRequestDto {
    private final String name;
    private final String info;
    private final Integer price;
}
