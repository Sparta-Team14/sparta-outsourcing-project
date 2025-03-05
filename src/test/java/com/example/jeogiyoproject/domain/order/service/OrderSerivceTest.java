package com.example.jeogiyoproject.domain.order.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.order.dto.request.CreateOrderRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.FindOrdersRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.OrderMenuRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import com.example.jeogiyoproject.domain.order.dto.response.FindOrdersResponseDto;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import com.example.jeogiyoproject.domain.order.repository.OrderDetailRepository;
import com.example.jeogiyoproject.domain.order.repository.OrderRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
public class OrderSerivceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private FoodStoreRepository foodStoreRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private OrderService orderService;

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

    @Nested
    class 주문_생성 {
        List<OrderMenuRequestDto> items = List.of(
                new OrderMenuRequestDto(1L, 1),
                new OrderMenuRequestDto(2L, 2));

        String request = "request";

        CreateOrderRequestDto dto = new CreateOrderRequestDto(items, request);

        @Test
        void 존재하지_않는_가게_조회_시_예외_발생() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.createOrder(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.FOODSTORE_NOT_FOUND);
        }

        @Test
        void 가게의_영업_시간이_아닌_경우_예외_발생() {
            // given
            ReflectionTestUtils.setField(foodstore, "openAt", LocalTime.of(9, 0));
            ReflectionTestUtils.setField(foodstore, "closeAt", LocalTime.of(9, 30));
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.createOrder(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.ORDER_BAD_REQUEST);
        }

        @Test
        void 존재하지_않는_메뉴_주문_시_예외_발생() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));
            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.createOrder(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.MENU_NOT_FOUND);
        }

        @Test
        void 주문_메뉴가_해당_가게의_메뉴가_아닌_경우_예외_발생() {
            // given
            ReflectionTestUtils.setField(foodstore, "id", 1L);
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));

            ReflectionTestUtils.setField(foodstore, "id", 2L);
            MenuCategory otherMenuCategory = new MenuCategory("name", foodstore);
            Menu otherFoodStoreMenu = new Menu(otherMenuCategory, "name", "info", 1);
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(otherFoodStoreMenu));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.createOrder(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.ORDER_BAD_REQUEST);
        }

        @Test
        void 최소_금액을_만족하지_못한_경우_예외_발생() {
            // given
            ReflectionTestUtils.setField(foodstore, "id", 1L);
            ReflectionTestUtils.setField(foodstore, "minPrice", 99999);
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.createOrder(authUser, foodstoreId, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.ORDER_BAD_REQUEST);
        }

        @Test
        void 성공() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));
            ReflectionTestUtils.setField(foodstore, "id", 1L);
            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(orderRepository.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            CreateOrderResponseDto response = orderService.createOrder(authUser, foodstoreId, dto);
            // then
            assertNotNull(response);
            assertEquals(response.getFoodstoreId(), foodstore.getId());
            assertEquals(response.getStatus(), Status.WAIT.name());
        }
    }

    @Nested
    class 주문_목록_조회 {
        FindOrdersRequestDto dto = new FindOrdersRequestDto();

        @Test
        void 해당_가게의_사장이_아닌_경우_예외_발생() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.findAllOrders(authUser, foodstoreId, 1, 10, dto)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.NOT_FOODSTORE_OWNER);
        }

        @Test
        void 성공() {
            // given
            Order order = new Order(foodstore,
                    User.fromAuthUser(authUser),
                    1,
                    1,
                    "request"
            );
            Page<Order> orders = new PageImpl<>(List.of(order));
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));
            given(orderRepository.findAllByFoodstoreIdByCreatedAtDesc(
                    any(Pageable.class),
                    anyLong(),
                    anyList(),
                    any(LocalDateTime.class),
                    any(LocalDateTime.class)
            )).willReturn(orders);

            // when
            Page<FindOrdersResponseDto> response = orderService.findAllOrders(authOwner, foodstoreId, 1, 10, dto);

            // then
            assertNotNull(response);
            assertEquals(response.getTotalElements(), 1);
        }
    }

    @Nested
    class 주문_조회 {
        @Test
        void 해당_가게의_사장이_아닌_경우_예외_발생() {
            // given
            given(foodStoreRepository.findById(anyLong())).willReturn(Optional.of(foodstore));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () ->
                    orderService.findOrder(authUser, foodstoreId, 1L)
            );
            assertEquals(exception.getErrorCode(), ErrorCode.NOT_FOODSTORE_OWNER);
        }

        @Test
        void 주문이_존재하지_않는_경우_예외_발생() {
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
    class 주문_상태_변경 {
        @Test
        void 해당_가게의_사장이_아닌_경우_예외_발생() {
            // given

            // when & then
        }

        @Test
        void 주문이_존재하지_않는_경우_예외_발생() {
            // given

            // when & then
        }

        @Test
        void 취소_상태를_변경하는_경우_예외_발생() {
            // given

            // when & then
        }

        @Test
        void 상태를_전_단계로_돌리는_경우_예외_발생() {
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
    class 주문이력_목록_조회 {
        @Test
        void 성공() {
            // given

            // when

            // then
        }
    }

    @Nested
    class 주문이력_단건_조회 {
        @Test
        void 주문이_존재하지_않는_경우_예외_발생() {
            // given

            // when

            // then
        }

        @Test
        void 해당_주문의_주문자가_아닌_경우_예외_발생() {
            // given

            // when

            // then
        }

        @Test
        void 성공() {
            // given

            // when

            // then
        }
    }

    @Nested
    class 주문_취소 {
        @Test
        void 주문이_존재하지_않는_경우_예외_발생() {
            // given

            // when

            // then
        }

        @Test
        void 해당_주문의_주문자가_아닌_경우_예외_발생() {
            // given

            // when

            // then
        }

        @Test
        void 주문_상태가_대기가_아닌_경우_예외_발생() {
            // given

            // when

            // then
        }

        @Test
        void 성공() {
            // given

            // when

            // then
        }
    }


}
