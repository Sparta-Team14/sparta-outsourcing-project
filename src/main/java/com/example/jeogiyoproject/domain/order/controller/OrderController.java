package com.example.jeogiyoproject.domain.order.controller;

import com.example.jeogiyoproject.domain.order.dto.request.*;
import com.example.jeogiyoproject.domain.order.dto.response.*;
import com.example.jeogiyoproject.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    // TODO 사용자 권한
    @PostMapping("/foodstores/{foodstoreId}/orders")
    public ResponseEntity<CreateOrderResponseDto> createOrder(@PathVariable Long foodstoreId,
                                                              @RequestBody CreateOrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(foodstoreId, dto));
    }

    // 주문 목록 조회
    // TODO 사장님 권한
    @GetMapping("/foodstores/{foodstoreId}/orders")
    public ResponseEntity<Page<FindOrdersResponseDto>> findAllOrders(@PathVariable Long foodstoreId,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestBody FindOrdersRequestDto dto) {
        return ResponseEntity.ok(orderService.findAllOrders(foodstoreId, page, size, dto));
    }

    // 주문 조회
    // TODO 사장님 권한
    @GetMapping("/foodstores/{foodstoreId}/orders/{orderId}")
    public ResponseEntity<FindOrderResponseDto> findOrder(@PathVariable Long foodstoreId,
                                                          @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrder(foodstoreId, orderId));
    }

    // 주문 상태 변경
    // TODO 사장님 권한
    @PatchMapping("/foodstores/{foodstoreId}/orders/{orderId}")
    public ResponseEntity<String> changeStatus(@PathVariable Long foodstoreId,
                                               @PathVariable Long orderId,
                                               @RequestBody ChangeStatusRequestDto dto) {
        return ResponseEntity.ok(orderService.changeStatus(foodstoreId, orderId, dto));
    }

    // 주문이력 확인(사용자)
    // TODO 사용자 권한
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderHistoryResponseDto>> findOrdersByUser(@RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestBody OrderHistoryRequestDto dto) {
        return ResponseEntity.ok(orderService.findOrdersByUser(page, size, dto));
    }

    // 주문이력 단건 조회(사용자)
    // TODO 사용자 권한
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<FindOrderByUserResponseDto> findOrderByUser(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderByUser(orderId));
    }

    // 리뷰 생성 ( 사용자 )
    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<CreateReviewResponseDto> createReview(@PathVariable Long ordersId,
                                                @RequestBody CreateReviewRequestDto dto) {
        return ResponseEntity.ok(orderService.createReview(ordersId, dto));
    }

    // 주문 취소(사용자)
    // TODO 사용자 권한
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }


}
