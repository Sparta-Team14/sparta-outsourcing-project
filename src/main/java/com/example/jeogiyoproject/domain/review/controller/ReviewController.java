package com.example.jeogiyoproject.domain.review.controller;

import com.example.jeogiyoproject.domain.order.dto.request.CreateReviewRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateReviewResponseDto;
import com.example.jeogiyoproject.domain.review.dto.response.ReviewPageResponseDto;
import com.example.jeogiyoproject.domain.review.dto.response.ReviewResponseDto;
import com.example.jeogiyoproject.domain.review.service.ReviewService;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 생성 ( 사용자 )
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<CreateReviewResponseDto> createReview(@Auth AuthUser authUser,
                                                                @PathVariable Long orderId,
                                                                @RequestBody CreateReviewRequestDto dto) {
        return ResponseEntity.ok(reviewService.createReview(authUser, orderId, dto));
    }

    // 리뷰 조회
    @GetMapping("/reviews")
    public ResponseEntity<ReviewPageResponseDto> findAllReviews(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
             @RequestParam(name = "rating", required = false) Integer rating
    ) {
        LocalDateTime startDateTime = startDate != null ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? LocalDate.parse(endDate).atTime(23, 59, 59) : null;

        ReviewPageResponseDto responseDto = reviewService.findAll(startDateTime, endDateTime, page, size, rating);
        return ResponseEntity.ok(responseDto);
    }

}

