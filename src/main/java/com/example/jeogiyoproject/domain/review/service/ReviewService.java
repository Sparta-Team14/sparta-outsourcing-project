package com.example.jeogiyoproject.domain.review.service;

import com.example.jeogiyoproject.domain.review.dto.response.ReviewPageResponseDto;
import com.example.jeogiyoproject.domain.review.dto.response.ReviewResponseDto;
import com.example.jeogiyoproject.domain.review.entity.Review;
import com.example.jeogiyoproject.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public ReviewPageResponseDto findAll(LocalDateTime startDate, LocalDateTime endDate, int page, int size, int rating) {
        // page를 0이하로 입력하면 첫번째 페이지 반환
        int adjustedPage = (page > 0) ? page - 1 : 0;
        // 수정일 기준으로 내림차순 정렬
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").ascending());
        Page<Review> reviewPage;

        // startDate와 endDate를 입력했을 때 startDate가 endDate 보다 뒤면 예외처리
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalStateException("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
        }

        // startDate와 endDate 둘 중 하나라도 null 이면 전체 기간 조회
        if (startDate == null || endDate == null) {
            reviewPage = reviewRepository.findAll(pageable);
        } else {
            // startDate와 endDate 둘 다 입력하면 두 기간 사이 조회

            reviewPage = reviewRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        }

        Page<ReviewResponseDto> responseDto = reviewPage.map(review -> new ReviewResponseDto(
                review.getOrder().getId(),
                review.getRating(),
                review.getContents(),
                review.getCreatedAt()
        ));

        return new ReviewPageResponseDto(responseDto);

    }
}
