package com.example.jeogiyoproject.domain.review.service;

import com.example.jeogiyoproject.domain.order.dto.request.CreateReviewRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateReviewResponseDto;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import com.example.jeogiyoproject.domain.order.repository.OrderRepository;
import com.example.jeogiyoproject.domain.review.dto.response.ReviewPageResponseDto;
import com.example.jeogiyoproject.domain.review.dto.response.ReviewResponseDto;
import com.example.jeogiyoproject.domain.review.entity.Review;
import com.example.jeogiyoproject.domain.review.repository.ReviewRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public CreateReviewResponseDto createReview(AuthUser authUser, Long orderId, CreateReviewRequestDto dto) {
        User user = User.fromAuthUser(authUser);

        // 오더가 status 값을 갖는다.
        Order order = findOrder(orderId);

        // e2: 해당 주문의 주문자가 아닌 경우
        validOrderUser(user, order);

        // e3: 주문 상태가 완료가 아닌 경우 , status = 값을 가져와서 상태값이 컴플릿이 아니면 던진다
        if (order.getStatus() != Status.COMPLETED) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "주문 상태: " + order.getStatus());
        }

        Review review = new Review(dto.getRating(), dto.getContents(), order);
        Review savedOrder = reviewRepository.save(review);
        return new CreateReviewResponseDto(savedOrder.getOrder().getId(), savedOrder.getId(), savedOrder.getRating(),
                savedOrder.getContents(), savedOrder.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public ReviewPageResponseDto findAll(LocalDateTime startDate, LocalDateTime endDate, int page, int size, Integer rating)
    {
        // page를 0이하로 입력하면 첫번째 페이지 반환
        int adjustedPage = (page > 0) ? page - 1 : 0;
        // 수정일 기준으로 내림차순 정렬
        PageRequest pageable = PageRequest.of(adjustedPage, size, Sort.by("createdAt").descending());
        Page<Review> reviewPage;

        // startDate와 endDate를 입력했을 때 startDate가 endDate 보다 뒤면 예외처리
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalStateException("시작 날짜는 종료 날짜보다 이후일 수 없습니다.");
        }

        if (startDate == null || endDate == null) {
            reviewPage = (rating != null)
                    ? reviewRepository.findAllByRating(rating, pageable) // 특정 별점만 조회
                    : reviewRepository.findAllByRatingBetween(1, 5, pageable); // 전체 (1~5점) 조회
        } else {
            reviewPage = (rating != null)
                    ? reviewRepository.findAllByCreatedAtBetweenAndRating(startDate, endDate, rating, pageable) // 특정 기간 & 특정 별점 조회
                    : reviewRepository.findAllByCreatedAtBetween(startDate, endDate, pageable); // 특정 기간 내 모든 리뷰 조회
        }

        Page<ReviewResponseDto> responseDto = reviewPage.map(review -> new ReviewResponseDto(
                review.getId(), review.getOrder().getId(),
                review.getRating(),
                review.getContents(),
                review.getCreatedAt()
        ));

        return new ReviewPageResponseDto(responseDto);

    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND,
                        "주문번호:" + orderId));
    }

    private void validOrderUser(User user, Order order) {
        if (!user.getId().equals(order.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_ORDER_USER_ID, "회원번호: " + user.getId());
        }
    }
}

