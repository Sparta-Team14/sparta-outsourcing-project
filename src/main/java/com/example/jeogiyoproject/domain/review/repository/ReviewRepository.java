package com.example.jeogiyoproject.domain.review.repository;

import com.example.jeogiyoproject.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Review> findAllByRating(Integer rating, Pageable pageable);

    Page<Review> findAllByRatingBetween(Integer minRating, Integer maxRating, Pageable pageable);

    Page<Review> findAllByCreatedAtBetweenAndRating(LocalDateTime startDate, LocalDateTime endDate, Integer rating, Pageable pageable);
}
//필드연결
// 매개변수 에 따른 조회 수백개하는데 별점다양한데 하나밖에 보여줄수없음