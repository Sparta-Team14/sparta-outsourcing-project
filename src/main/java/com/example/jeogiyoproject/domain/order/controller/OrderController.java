package com.example.jeogiyoproject.domain.order.controller;

import com.example.jeogiyoproject.domain.order.dto.request.ChangeOrderStatusRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.CreateOrderRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.FindOrdersRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.OrderHistoryRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.*;
import com.example.jeogiyoproject.domain.order.service.OrderService;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.web.aop.annotation.OrderLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @OrderLogging
    @PostMapping("/foodstores/{foodstoreId}/orders")
    public ResponseEntity<CreateOrderResponseDto> createOrder(@Auth AuthUser authUser,
                                                              @PathVariable Long foodstoreId,
                                                              @RequestBody CreateOrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(authUser, foodstoreId, dto));
    }

    // 주문 목록 조회
    // TODO 사장님 권한
    @GetMapping("/foodstores/{foodstoreId}/orders")
    public ResponseEntity<Page<FindOrdersResponseDto>> findAllOrders(@Auth AuthUser authUser,
                                                                     @PathVariable Long foodstoreId,
                                                                     @RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestBody FindOrdersRequestDto dto) {
        return ResponseEntity.ok(orderService.findAllOrders(authUser, foodstoreId, page, size, dto));
    }

    // 주문 조회
    // TODO 사장님 권한
    @GetMapping("/foodstores/{foodstoreId}/orders/{orderId}")
    public ResponseEntity<FindOrderResponseDto> findOrder(@Auth AuthUser authUser,
                                                          @PathVariable Long foodstoreId,
                                                          @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrder(authUser, foodstoreId, orderId));
    }

    // 주문 상태 변경
    // TODO 사장님 권한
    @OrderLogging
    @PatchMapping("/foodstores/{foodstoreId}/orders/{orderId}")
    public ResponseEntity<ChangeOrderStatusResponseDto> changeStatus(@Auth AuthUser authUser,
                                                                     @PathVariable Long foodstoreId,
                                                                     @PathVariable Long orderId,
                                                                     @RequestBody ChangeOrderStatusRequestDto dto) {
        return ResponseEntity.ok(orderService.changeStatus(authUser, foodstoreId, orderId, dto));
    }

    // 주문이력 확인(사용자)
    // TODO 사용자 권한
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderHistoryResponseDto>> findOrdersByUser(@Auth AuthUser authUser,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestBody OrderHistoryRequestDto dto) {
        return ResponseEntity.ok(orderService.findOrdersByUser(authUser, page, size, dto));
    }

    // 주문이력 단건 조회(사용자)
    // TODO 사용자 권한
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<FindOrderByUserResponseDto> findOrderByUser(@Auth AuthUser authUser,
                                                                      @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderByUser(authUser, orderId));
    }

    // 주문 취소(사용자)
    // TODO 사용자 권한
    @OrderLogging
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ChangeOrderStatusResponseDto> cancelOrder(@Auth AuthUser authUser,
                                                                    @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(authUser, orderId));
    }


}
