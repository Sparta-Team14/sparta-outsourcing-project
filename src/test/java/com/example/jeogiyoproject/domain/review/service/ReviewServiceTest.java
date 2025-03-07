package com.example.jeogiyoproject.domain.review.service;

import com.example.jeogiyoproject.domain.order.dto.request.CreateReviewRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateReviewResponseDto;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import com.example.jeogiyoproject.domain.order.repository.OrderRepository;
import com.example.jeogiyoproject.domain.review.entity.Review;
import com.example.jeogiyoproject.domain.review.repository.ReviewRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class ReviewServiceTest {

    // 한 메서드에서 다 따로 값주고 검증하지 말고 값 한번에 주고 한 메서드 이어서 검증하기.
    // 방법 , 언제, 결과
    // given(구문정의) 내가할것 같은데), when, then
    // 준비 - 실행 - 검증
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Test
    void 리뷰를_생성할_수_있다() {
    // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);

        Long orderId = 2L;
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", orderId);
        ReflectionTestUtils.setField(order, "user", user);
        ReflectionTestUtils.setField(order, "status", Status.COMPLETED);

        Integer rating = 3;
        String contents = "하하하";

        CreateReviewRequestDto dto = new CreateReviewRequestDto(rating, contents);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(reviewRepository.save(any(Review.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        CreateReviewResponseDto responseDto = reviewService.createReview(authUser, orderId, dto);
    // Then
        assertNotNull(responseDto);
        assertEquals(order.getId(), responseDto.getOrdersId());
        assertEquals(dto.getRating(), responseDto.getRating());
    }
}

