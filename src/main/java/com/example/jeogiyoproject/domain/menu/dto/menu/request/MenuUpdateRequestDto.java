package com.example.jeogiyoproject.domain.menu.dto.menu.request;

import lombok.Getter;

@Getter
public class MenuUpdateRequestDto {
    private String name;
    private String info;
    private Integer price;
}
