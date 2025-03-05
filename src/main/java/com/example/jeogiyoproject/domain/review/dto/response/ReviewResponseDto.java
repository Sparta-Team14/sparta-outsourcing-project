package com.example.jeogiyoproject.domain.review.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewResponseDto {
    private final Long reviewId;
    private final Long ordersId;
    private final int rating;
    private final String contents;
    private final LocalDateTime createdAt;


    public ReviewResponseDto(Long reviewId, Long ordersId, int rating, String contents, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.ordersId = ordersId;
        this.rating = rating;
        this.contents = contents;
        this.createdAt = createdAt;
    }
}

