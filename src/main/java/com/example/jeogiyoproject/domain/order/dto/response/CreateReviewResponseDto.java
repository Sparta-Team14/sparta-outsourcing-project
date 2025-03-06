package com.example.jeogiyoproject.domain.order.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateReviewResponseDto {
    private final Long ordersId;
    private final Long reviewId;
    private final Integer rating;
    private final String contents;
    private final LocalDateTime createdAt;


    public CreateReviewResponseDto(Long ordersId, Long reviewId, Integer rating, String contents, LocalDateTime createdAt) {
        this.ordersId = ordersId;
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.createdAt = createdAt;

    }

}
