package com.example.jeogiyoproject.domain.menu.dto.menu.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DeletedMenuDto {
    private final Long id;
    private final String name;
    private final String info;
    private final Integer price;
    private final LocalDateTime deletedAt;
}
