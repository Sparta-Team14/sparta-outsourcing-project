package com.example.jeogiyoproject.domain.menu.dto.menu.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuBasicDto {
    private final Long id;
    private final String name;
    private final String info;
    private final Integer price;
}
