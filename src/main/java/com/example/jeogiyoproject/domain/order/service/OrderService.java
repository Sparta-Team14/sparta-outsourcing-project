package com.example.jeogiyoproject.domain.order.service;

import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.order.dto.request.ChangeStatusRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.CreateOrderRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.FindOrdersRequestDto;
import com.example.jeogiyoproject.domain.order.dto.request.OrderMenuRequestDto;
import com.example.jeogiyoproject.domain.order.dto.response.CreateOrderResponseDto;
import com.example.jeogiyoproject.domain.order.dto.response.FindOrdersResponseDto;
import com.example.jeogiyoproject.domain.order.dto.response.OrderResponseDto;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import com.example.jeogiyoproject.domain.order.enums.Status;
import com.example.jeogiyoproject.domain.order.repository.OrderDetailRepository;
import com.example.jeogiyoproject.domain.order.repository.OrderRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final FoodStoreRepository foodStoreRepository;
//    private final MenuRepository menuRepository;

    @Transactional
    public CreateOrderResponseDto createOrder(Long foodstoreId, CreateOrderRequestDto dto) {
        // TODO 유저 데이터 -> account?
        User user = new User();

        // e1: 존재하지 않는 가게 조회 시
        FoodStore foodStore = findFoodStore(foodstoreId);

        // e2: 가게의 오픈 시간이 아닌 경우
        if (isNotOperatingTime(foodStore.getOpenAt(), foodStore.getCloseAt())) {
            throw new CustomException(ErrorCode.ORDER_BAD_REQUEST,
                    "가게의 운영시간이 아닙니다. 오픈: " + foodStore.getOpenAt() +
                            ", 마감: " + foodStore.getCloseAt());
        }

        // e3: 메뉴 유효성 검증
        int totalPrice = 0;
        Map<Menu, Integer> menuMap = new HashMap<>();
        for (OrderMenuRequestDto menuRequest : dto.getMenus()) {
/*
            // TODO menu 구현 후 검증
            // e3-1: 존재하지 않는 메뉴 주문 시
            Menu menu = menuRepository.findById(menuRequest.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND,
                            "메뉴번호:" + menuRequest.getMenuId()));

            // e3-2: 주문 메뉴가 해당 가게의 메뉴가 아닌 경우
            if (!foodstoreId.equals(menu.getFoodStore().getId())) {
                throw new CustomException(ErrorCode.ORDER_BAD_REQUEST,
                        "해당 가게의 메뉴가 아닙니다. 메뉴번호: " + menuRequest.getMenuId());
            }
            totalPrice += menu.getPrice() * menuRequest.getAmount();
            menuMap.put(menu, menuRequest.getAmount());
            */
        }

        // e4: 최소 금액을 만족하지 못한 경우
        if (totalPrice < foodStore.getMinPrice()) {
            throw new CustomException(ErrorCode.ORDER_BAD_REQUEST,
                    "최소 주문금액 미만. 최소 주문금액: " + foodStore.getMinPrice());
        }

        Order order = new Order(foodStore, user, totalPrice, dto.getRequest());
        Order saveOrder = orderRepository.save(order);
        menuMap.forEach((menu, amount) -> {
            orderDetailRepository.save(new OrderDetail(saveOrder, menu, amount));
        });

        return CreateOrderResponseDto.fromOrder(saveOrder);
    }

    @Transactional(readOnly = true)
    public Page<FindOrdersResponseDto> findAllOrders(Long foodstoreId, int page, int size, FindOrdersRequestDto dto) {
        // TODO 유저 데이터
        User user = new User();

        // e1: 해당 가게의 사장이 아닌 경우
        validFoodstoreOwner(foodstoreId, user);

        Pageable pageable = PageRequest.of(page - 1, size);
        Status status = Status.of(dto.getStatus());
        LocalDate endAt = dto.getEndAt();
        if (endAt != null) {
            endAt = endAt.plusDays(1);
        }
        Page<Order> orders = orderRepository.findAllByCreatedAtDesc(pageable, status, dto.getStartAt(), endAt);

        return orders.map(FindOrdersResponseDto::fromOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findOrder(Long foodstoreId, Long orderId) {
        // TODO 유저 데이터
        User user = new User();

        // e1: 해당 가게의 사장이 아닌 경우
        validFoodstoreOwner(foodstoreId, user);

        // e2: 주문이 존재하지 않는 경우
        Order order = findOrder(orderId);

        List<OrderDetail> orderDetail = orderDetailRepository.findAllByOrderId(orderId);

        return OrderResponseDto.fromOrderAndOrderDetail(order, orderDetail);
    }

    @Transactional
    public String changeStatus(Long foodstoreId, Long orderId, ChangeStatusRequestDto dto) {
        // TODO 유저 데이터
        User user = new User();

        // e1: 해당 가게의 사장이 아닌 경우
        validFoodstoreOwner(foodstoreId, user);

        // e2: 주문이 존재하지 않는 경우
        Order order = findOrder(orderId);

        Status status = Status.of(dto.getStatus());

        // e3: 취소 상태를 변경하는 경우
        if (order.getStatus() == Status.CANCELED) {
            throw new CustomException(ErrorCode.CHANGE_STATUS_ERROR, "취소 주문은 변경할 수 없습니다.");
        }

        // e4: 상태를 전 단계로 돌리는 경우
        if (order.getStatus().getLevel() >= status.getLevel()) {
            throw new CustomException(ErrorCode.CHANGE_STATUS_ERROR, "상태를 전 단계로 변경할 수 없습니다.");
        }

        order.updateStatus(status);
        return "주문 상태가 변경되었습니다. 주문번호: " + orderId + ", 상태: " + status;
    }

    public Page<FindOrdersResponseDto> findOrdersByUser(FindOrdersRequestDto dto) {
        return null;
    }

    public String cancelOrder(Long orderId) {
        return null;
    }

    private FoodStore findFoodStore(Long foodstoreId) {
        return foodStoreRepository.findById(foodstoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND,
                        "가게번호: " + foodstoreId));
    }

    private boolean isNotOperatingTime(LocalTime openAt, LocalTime closeAt) {
        LocalTime now = LocalTime.now();
        if (!now.isAfter(openAt)) {
            return false;
        }
        if (!now.isBefore(closeAt)) {
            return false;
        }
        return true;
    }

    private void validFoodstoreOwner(Long foodstoreId, User user) {
        FoodStore foodStore = findFoodStore(foodstoreId);
        // TODO 해당 가게의 사장이 아닌 경우
        /*
        if (foodStore.getAccount().getId() != user.getId()) {
           throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER, "userId: " + user.getId());
        }
        */
    }

    private Order findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND,
                        "주문번호:" + orderId));
        return order;
    }
}
