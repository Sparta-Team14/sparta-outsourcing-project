package com.example.jeogiyoproject.domain.cart.service;

import com.example.jeogiyoproject.domain.cart.dto.request.CartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.response.CartResponseDto;
import com.example.jeogiyoproject.domain.cart.repository.CartItemsRepository;
import com.example.jeogiyoproject.domain.cart.repository.CartRepository;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    @Transactional
    public CartResponseDto addCart(Long foodstoreId, CartRequestDto dto) {
        return null;
    }

    @Transactional(readOnly = true)
    public CartResponseDto findCart() {
        return null;
    }

    @Transactional
    public CartResponseDto updateCart(Long cartId, CartRequestDto dto) {
        return null;
    }

    public CreateOrderResponseDto createOrderByCart(Long cartId) {
        return null;
    }

    //    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void deleteExpiredCartItems() {
        LocalDateTime cutOffTime = LocalDateTime.now().minusHours(24);

        // 장바구니 만료 품목 삭제
        // 장바구니에 남은 품목이 없는 경우 장바구니 삭제

    }
}
