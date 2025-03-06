package com.example.jeogiyoproject.domain.order.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReviewRequestDto {

    private int rating;
    private String contents;

}
