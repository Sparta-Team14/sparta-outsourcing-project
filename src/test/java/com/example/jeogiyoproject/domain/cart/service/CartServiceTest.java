package com.example.jeogiyoproject.domain.cart.service;

import com.example.jeogiyoproject.domain.cart.dto.request.CartRequestDto;
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
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemsRepository cartItemsRepository;
    @Mock
    private FoodStoreRepository foodStoreRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private CartService cartService;

    AuthUser authOwner = new AuthUser(1L, "a@a.com", UserRole.OWNER);
    AuthUser authUser = new AuthUser(2L, "a@a.com", UserRole.USER);

    FoodStore foodstore = new FoodStore(
            User.fromAuthUser(authOwner),
            "title", "address",
            1,
            LocalTime.of(0, 0),
            LocalTime.of(23, 59));
    Long foodstoreId = 1L;

    MenuCategory menuCategory = new MenuCategory("name", foodstore);
    Menu menu = new Menu(menuCategory, "name", "info", 1);
    Long menuId = 1L;

    @Nested
    class 장바구니_담기 {
        Long menuId = 1L;
        Integer quantity = 1;

        CartRequestDto dto = new CartRequestDto(menuId, quantity);
        @Test
        void 존재하지_않는_가게_조회_시_예외처리() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.empty());
            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    cartService.addCart(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.FOODSTORE_NOT_FOUND);
        }

        @Test
        void 존재하지_않는_메뉴_요청_시_예외처리() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));
            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    cartService.addCart(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.MENU_NOT_FOUND);
        }

        @Test
        void 요청_메뉴가_해당_가게의_메뉴가_아닌_경우_예외처리() {
            // given
            ReflectionTestUtils.setField(foodstore, "id", 1L);
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));

            ReflectionTestUtils.setField(foodstore, "id", 2L);
            MenuCategory otherMenuCategory = new MenuCategory("name", foodstore);
            Menu otherFoodStoreMenu = new Menu(otherMenuCategory, "name", "info", 1);
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(otherFoodStoreMenu));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    cartService.addCart(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.ORDER_BAD_REQUEST);
        }

        @Test
        void 담기_성공() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));
            ReflectionTestUtils.setField(foodstore, "id", 1L);
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(cartRepository.save(any(Cart.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(cartItemsRepository.save(any(CartItems.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            CartResponseDto response = cartService.addCart(authUser, foodstoreId, dto);

            // then
            assertNotNull(response);
            assertEquals(response.getFoodstoreId(), foodstore.getId());
        }
    }

    @Nested
    class 장바구니_조회 {
        @Test
        void 장바구니가_존재하지_않는_경우_예외처리() {
            // given
            given(cartRepository.findByUserId(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    cartService.findCart(authUser)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.CART_NOT_FOUND);
        }

        @Test
        void 성공() {
            // given
            Cart cart = new Cart(User.fromAuthUser(authUser), foodstore);
            ReflectionTestUtils.setField(cart, "id", 1L);
            CartItems cartItems = new CartItems(cart, menu, 1);

            given(cartRepository.findByUserId(anyLong())).willReturn(Optional.of(cart));
            given(cartItemsRepository.findByCartId(anyLong())).willReturn(List.of(cartItems));

            // when
            CartResponseDto response = cartService.findCart(authUser);

            // then
            assertNotNull(response);
            assertEquals(response.getFoodstoreId(), foodstore.getId());
            assertEquals(response.getCartId(), cart.getId());
        }
    }

    @Nested
    class 장바구니_수정 {
        Cart cart = new Cart(User.fromAuthUser(authUser), foodstore);
        Long cartId = 1L;
        CartItems cartItems = new CartItems(cart, menu, 1);

        List<UpdateCartItemsRequestDto> updateCartItems = List.of(new UpdateCartItemsRequestDto(menuId, 1));
        UpdateCartRequestDto dto = new UpdateCartRequestDto(updateCartItems);
        @Test
        void 장바구니가_존재하지_않는_경우_예외처리() {
            // given
            ReflectionTestUtils.setField(cart, "id", cartId);
            given(cartRepository.findByUserId(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    cartService.updateCart(authUser, cartId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.CART_NOT_FOUND);
        }

        @Test
        void 장바구니_유저와_조회_유저가_일치하지_않는_경우_예외처리() {
            // given

            // when & then
        }

        @Test
        void 존재하지_않는_메뉴_요청_시_예외처리() {
            // given

            // when & then
        }

        @Test
        void 성공() {
            // given

            // when

            // then
        }
    }

    @Nested
    class 장바구니_주문 {
        @Test
        void 장바구니가_존재하지_않는_경우_예외처리() {
            // given

            // when & then
        }

        @Test
        void 장바구니_유저와_조회_유저가_일치하지_않는_경우_예외처리() {
            // given

            // when & then
        }

        @Test
        void 존재하지_않는_메뉴_요청_시_예외처리() {
            // given

            // when & then
        }

        @Test
        void 성공() {
            // given

            // when

            // then
        }
    }

    @Nested
    class 만료_장바구니_삭제 {
        @Test
        void 성공() {
            // given

            // when

            // then
        }
    }
}
