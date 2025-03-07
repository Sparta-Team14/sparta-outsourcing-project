package com.example.jeogiyoproject.domain.cart.controller;

import com.example.jeogiyoproject.domain.cart.dto.request.CartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.request.CreateOrderByCartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.request.UpdateCartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.response.CartResponseDto;
import com.example.jeogiyoproject.domain.cart.service.CartService;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.web.aop.annotation.AuthCheck;
import com.example.jeogiyoproject.web.aop.annotation.OrderLogging;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @AuthCheck(UserRole.USER)
    @PostMapping("/foodstores/{foodstoreId}/carts")
    public ResponseEntity<CartResponseDto> addCart(@Auth AuthUser authUser,
                                                   @PathVariable Long foodstoreId,
                                                   @RequestBody CartRequestDto dto) {
        return ResponseEntity.ok(cartService.addCart(authUser, foodstoreId, dto));
    }

    // 장바구니 조회
    @AuthCheck(UserRole.USER)
    @GetMapping("/carts")
    public ResponseEntity<CartResponseDto> findCart(@Auth AuthUser authUser) {
        return ResponseEntity.ok(cartService.findCart(authUser));
    }

    // 장바구니 수정
    @AuthCheck(UserRole.USER)
    @PostMapping("/carts/{cartId}")
    public ResponseEntity<CartResponseDto> updateCart(@Auth AuthUser authUser,
                                                      @PathVariable Long cartId,
                                                      @RequestBody UpdateCartRequestDto dto) {
        return ResponseEntity.ok(cartService.updateCart(authUser, cartId, dto));
    }

    // 장바구니 주문
    @AuthCheck(UserRole.USER)
    @OrderLogging
    @PostMapping("/carts/{cartId}/order")
    public ResponseEntity<CreateOrderResponseDto> createOrderByCart(@Auth AuthUser authUser,
                                                                    @PathVariable Long cartId,
                                                                    @RequestBody CreateOrderByCartRequestDto dto) {
        return ResponseEntity.ok(cartService.createOrderByCart(authUser, cartId, dto));
    }
}
