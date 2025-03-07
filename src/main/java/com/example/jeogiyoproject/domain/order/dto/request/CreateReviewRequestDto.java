package com.example.jeogiyoproject.domain.order.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReviewRequestDto {

    private Integer rating;
    private String contents;

    public CreateReviewRequestDto(int rating, String contents) {
        this.rating = rating;
        this.contents = contents;
    }
}
