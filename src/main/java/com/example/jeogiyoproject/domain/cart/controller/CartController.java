package com.example.jeogiyoproject.domain.cart.controller;

import com.example.jeogiyoproject.domain.cart.dto.request.CartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.response.CartResponseDto;
import com.example.jeogiyoproject.domain.cart.service.CartService;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import com.example.jeogiyoproject.web.aop.annotation.OrderLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    // TODO 사용자 권한
    @PostMapping("/foodstores/{foodstoreId}/carts")
    public ResponseEntity<CartResponseDto> addCart(@PathVariable Long foodstoreId,
                                                   @RequestBody CartRequestDto dto) {
        return ResponseEntity.ok(cartService.addCart(foodstoreId, dto));
    }

    // 장바구니 조회
    // TODO 사용자 권한
    @GetMapping("/carts")
    public ResponseEntity<CartResponseDto> findCart() {
        return ResponseEntity.ok(cartService.findCart());
    }

    // 장바구니 수정
    // TODO 사용자 권한
    @PostMapping("/carts/{cartId}")
    public ResponseEntity<CartResponseDto> updateCart(@PathVariable Long cartId,
                                                      @RequestBody CartRequestDto dto) {
        return ResponseEntity.ok(cartService.updateCart(cartId, dto));
    }

    // 장바구니 주문
    // TODO 사용자 권한
    @OrderLogging
    @PostMapping("/carts/{cartId}/order")
    public ResponseEntity<CreateOrderResponseDto> createOrderByCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.createOrderByCart(cartId));
    }
}
