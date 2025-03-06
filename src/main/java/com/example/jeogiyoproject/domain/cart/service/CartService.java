package com.example.jeogiyoproject.domain.cart.service;

import com.example.jeogiyoproject.domain.cart.dto.request.CartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.request.CreateOrderByCartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.request.UpdateCartItemsRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.request.UpdateCartRequestDto;
import com.example.jeogiyoproject.domain.cart.dto.response.CartResponseDto;
import com.example.jeogiyoproject.domain.cart.entity.Cart;
import com.example.jeogiyoproject.domain.cart.entity.CartItems;
import com.example.jeogiyoproject.domain.cart.repository.CartItemsRepository;
import com.example.jeogiyoproject.domain.cart.repository.CartRepository;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.order.dto.request.CreateOrderRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.OrderMenuRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import com.example.jeogiyoproject.domain.order.service.OrderService;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final FoodStoreRepository foodStoreRepository;
    private final MenuRepository menuRepository;
    private final OrderService orderService;


    @Transactional
    public CartResponseDto addCart(AuthUser authUser, Long foodstoreId, CartRequestDto dto) {
        User user = User.fromAuthUser(authUser);

        // e1: 요청 가게가 존재하지 않는 경우
        FoodStore foodStore = findFoodStore(foodstoreId);

        // e2: 요청 메뉴가 존재하지 않는 경우
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND,
                        "메뉴번호:" + dto.getMenuId()));

        // e3: 요청 메뉴가 해당 가게의 메뉴가 아닌 경우
        if (!foodstoreId.equals(menu.getMenuCategory().getFoodStore().getId())) {
            throw new CustomException(ErrorCode.ORDER_BAD_REQUEST,
                    "해당 가게의 메뉴가 아닙니다. 메뉴번호: " + menu.getId());
        }
        Integer quantity = dto.getQuantity();

        Optional<Cart> findCart = cartRepository.findByUserId(user.getId());
        Cart cart;
        if (findCart.isPresent()) {
            cart = findCart.get();
            cart.setUpdatedAt(LocalDateTime.now());
        } else {
            cart = new Cart(user, foodStore);
        }
        cartRepository.save(cart);

        CartItems cartItem = cartItemsRepository.findByCartIdAndMenuId(cart.getId(), menu.getId())
                .map(item -> {
                    item.updateQuantity(item.getQuantity() + quantity); // 기존 수량에 추가
                    return item;
                })
                .orElse(new CartItems(cart, menu, quantity));

        cartItemsRepository.save(cartItem);

        List<CartItems> cartItems = cartItemsRepository.findByCartId(cart.getId());
        return CartResponseDto.fromCartAndCartItems(cart, cartItems);
    }

    @Transactional(readOnly = true)
    public CartResponseDto findCart(AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        // e1: 장바구니가 존재하지 않는 경우
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        List<CartItems> cartItems = cartItemsRepository.findByCartId(cart.getId());

        return CartResponseDto.fromCartAndCartItems(cart, cartItems);
    }

    @Transactional
    public CartResponseDto updateCart(AuthUser authUser, Long cartId, UpdateCartRequestDto dto) {
        User user = User.fromAuthUser(authUser);

        // e1: 장바구니 조회 및 유효성 검사
        Cart cart = findByCartId(cartId, user, "장바구니 수정 권한이 없습니다.");

        List<CartItems> cartItems = cartItemsRepository.findByCartId(cartId);

        Set<Long> cartItemIds = cartItems.stream()
                .map(CartItems::getId)
                .collect(Collectors.toSet());
        cartItemsRepository.deleteAllByIdInBatch(cartItemIds);

        List<CartItems> updateItems = new ArrayList<>();
        if (dto.getItems().isEmpty()) {
            cartRepository.deleteById(cartId);
        } else {
            for (UpdateCartItemsRequestDto item : dto.getItems()) {
                // e2: 요청 메뉴가 존재하지 않는 경우
                Menu menu = menuRepository.findById(item.getMenuId())
                        .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND,
                                "메뉴번호:" + item.getMenuId()));
                CartItems cartItem = new CartItems(cart, menu, item.getQuantity());
                updateItems.add(cartItem);
            }
            cartItemsRepository.saveAll(updateItems);
        }
        return CartResponseDto.fromCartAndCartItems(cart, updateItems);
    }

    public CreateOrderResponseDto createOrderByCart(AuthUser authUser, Long cartId, CreateOrderByCartRequestDto dto) {
        User user = User.fromAuthUser(authUser);

        // e1: 장바구니 조회 및 유효성 검사
        Cart cart = findByCartId(cartId, user, "장바구니 주문 권한이 없습니다.");

        // e2: 장바구니 품목이 존재하지 않는 경우
        List<CartItems> cartItems = cartItemsRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.CART_ITEMS_NOT_FOUND);
        }

        Map<Long, Integer> cartItemsMap = cartItems.stream().collect(Collectors.toMap(
                cartItem -> cartItem.getMenu().getId(),
                CartItems::getQuantity
        ));

        List<OrderMenuRequestDto> orderItems = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cartItemsMap.entrySet()) {
            OrderMenuRequestDto orderMenu = new OrderMenuRequestDto(entry.getKey(), entry.getValue());
            orderItems.add(orderMenu);
        }

        CreateOrderRequestDto orderRequestDto = CreateOrderRequestDto.builder()
                .items(orderItems)
                .request(dto.getRequest())
                .build();

        CreateOrderResponseDto order = orderService.createOrder(authUser, cart.getFoodstore().getId(), orderRequestDto);

        cartItemsRepository.deleteAll(cartItems);
        cartRepository.deleteById(cartId);

        return order;
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void deleteExpiredCartItems() {
        LocalDateTime cutOffTime = LocalDateTime.now().minusDays(1);
        List<Cart> findExpiredCart = cartRepository.findByUpdatedAtBefore(cutOffTime);
        if (findExpiredCart.isEmpty()) {
            // 삭제 대상이 없을 경우 만료 장바구니 삭제 스케줄 종료
            return;
        }
        List<Long> cartIds = findExpiredCart.stream().map(Cart::getId).toList();
        List<CartItems> cartItems = cartItemsRepository.findByCartIdIn(cartIds);

        // 장바구니 만료 품목 삭제
        cartItemsRepository.deleteAllInBatch(cartItems);
        // 장바구니에 남은 품목이 없는 경우 장바구니 삭제
        cartRepository.deleteAllByIdInBatch(cartIds);

        log.info("장바구니 만료 품목 삭제 수: {}", cartItems.size());
    }

    private Cart findByCartId(Long cartId, User user, String message) {
        // e1: 장바구니가 존재하지 않는 경우
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        // e2: 장바구니 유저와 조회 유저가 일치하지 않는 경우
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, message);
        }
        return cart;
    }

    private FoodStore findFoodStore(Long foodstoreId) {
        return foodStoreRepository.findById(foodstoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND,
                        "가게번호: " + foodstoreId));
    }
}
