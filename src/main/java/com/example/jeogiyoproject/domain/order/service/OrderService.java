package com.example.jeogiyoproject.domain.order.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.order.dto.request.*;
import com.example.jeogiyoproject.domain.order.dto.response.*;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import com.example.jeogiyoproject.domain.order.enums.Status;
import com.example.jeogiyoproject.domain.order.repository.OrderDetailRepository;
import com.example.jeogiyoproject.domain.order.repository.OrderRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface{

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final FoodStoreRepository foodStoreRepository;
    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public CreateOrderResponseDto createOrder(AuthUser authUser, Long foodstoreId, CreateOrderRequestDto dto) {
        User user = User.fromAuthUser(authUser);

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
        int totalQuantity = 0;
        Map<Menu, Integer> menuMap = new HashMap<>();
        for (OrderMenuRequestDto menuRequest : dto.getItems()) {

            // e3-1: 존재하지 않는 메뉴 주문 시
            Menu menu = menuRepository.findById(menuRequest.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND,
                            "메뉴번호:" + menuRequest.getMenuId()));

            // e3-2: 주문 메뉴가 해당 가게의 메뉴가 아닌 경우
            if (!foodstoreId.equals(menu.getMenuCategory().getFoodStore().getId())) {
                throw new CustomException(ErrorCode.ORDER_BAD_REQUEST,
                        "해당 가게의 메뉴가 아닙니다. 메뉴번호: " + menuRequest.getMenuId());
            }

            totalPrice += menu.getPrice() * menuRequest.getQuantity();
            totalQuantity += menuRequest.getQuantity();
            menuMap.put(menu, menuRequest.getQuantity());

        }

        // e4: 최소 금액을 만족하지 못한 경우
        if (totalPrice < foodStore.getMinPrice()) {
            throw new CustomException(ErrorCode.ORDER_BAD_REQUEST,
                    "최소 주문금액 미만. 최소 주문금액: " + foodStore.getMinPrice());
        }

        Order order = new Order(foodStore, user, totalPrice, totalQuantity, dto.getRequest());
        Order saveOrder = orderRepository.save(order);
        menuMap.forEach((menu, quantity) -> {
            orderDetailRepository.save(new OrderDetail(saveOrder, menu, quantity));
        });

        return CreateOrderResponseDto.fromOrder(saveOrder);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<FindOrdersResponseDto> findAllOrders(AuthUser authUser, Long foodstoreId, int page, int size, FindOrdersRequestDto dto) {
        User user = User.fromAuthUser(authUser);

        // e1: 해당 가게의 사장이 아닌 경우
        validFoodstoreOwner(foodstoreId, user);
        // e2: 조회 기간을 입력한 경우 조회기간 검증
        validDateTime(dto.getStartAt(), dto.getEndAt());

        Pageable pageable = PageRequest.of(page - 1, size);

        List<Status> statusList = new ArrayList<>();
        if (dto.getStatus() == null) {
            statusList = List.of(Status.values());
        } else {
            statusList.add(Status.of(dto.getStatus()));
        }

        LocalDateTime startAt = LocalDateTime.of(dto.getStartAt(), LocalTime.of(0, 0));
        LocalDateTime endAt = LocalDateTime.of(dto.getEndAt().plusDays(1), LocalTime.of(0, 0));

        Page<Order> orders = orderRepository.findAllByFoodstoreIdByCreatedAtDesc(pageable, foodstoreId, statusList, startAt, endAt);

        return new PaginationResponse<>(orders.map(FindOrdersResponseDto::fromOrder));
    }

    @Transactional(readOnly = true)
    public FindOrderResponseDto findOrder(AuthUser authUser, Long foodstoreId, Long orderId) {
        User user = User.fromAuthUser(authUser);

        // e1: 해당 가게의 사장이 아닌 경우
        validFoodstoreOwner(foodstoreId, user);

        // e2: 주문이 존재하지 않는 경우
        Order order = findOrder(orderId);

        List<OrderDetail> orderDetail = orderDetailRepository.findAllByOrderId(orderId);

        return FindOrderResponseDto.fromOrderAndOrderDetail(order, orderDetail);
    }

    @Transactional
    public ChangeOrderStatusResponseDto changeStatus(AuthUser authUser, Long foodstoreId, Long orderId, ChangeOrderStatusRequestDto dto) {
        User user = User.fromAuthUser(authUser);

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
        return ChangeOrderStatusResponseDto.fromOrder(order);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<OrderHistoryResponseDto> findOrdersByUser(AuthUser authUser, int page, int size, OrderHistoryRequestDto dto) {
        User user = User.fromAuthUser(authUser);

        // 조회 기간을 입력한 경우 조회기간 검증
        validDateTime(dto.getStartAt(), dto.getEndAt());

        Pageable pageable = PageRequest.of(page - 1, size);
        List<Status> statusList = new ArrayList<>();
        if (dto.getStatus() == null) {
            statusList = List.of(Status.values());
        } else {
            statusList.add(Status.of(dto.getStatus()));
        }

        LocalDateTime startAt = LocalDateTime.of(dto.getStartAt(), LocalTime.of(0, 0));
        LocalDateTime endAt = LocalDateTime.of(dto.getEndAt().plusDays(1), LocalTime.of(0, 0));

        Page<Order> orders = orderRepository.findAllByUserId(
                pageable,
                user.getId(),
                dto.getFoodstoreTitle(),
                statusList,
                startAt,
                endAt
        );

        return new PaginationResponse<>(orders.map(OrderHistoryResponseDto::fromOrder));
    }

    @Transactional(readOnly = true)
    public FindOrderByUserResponseDto findOrderByUser(AuthUser authUser, Long orderId) {
        User user = User.fromAuthUser(authUser);

        // e1: 주문이 존재하지 않는 경우
        Order order = findOrder(orderId);

        // e2: 해당 주문의 주문자가 아닌 경우
        validOrderUser(user, order);

        List<OrderDetail> orderDetail = orderDetailRepository.findAllByOrderId(orderId);

        return FindOrderByUserResponseDto.fromOrderAndOrderDetail(order, orderDetail);
    }

    @Transactional
    public ChangeOrderStatusResponseDto cancelOrder(AuthUser authUser, Long orderId) {
        User user = User.fromAuthUser(authUser);

        // e1: 주문이 존재하지 않는 경우
        Order order = findOrder(orderId);

        // e2: 해당 주문의 주문자가 아닌 경우
        validOrderUser(user, order);

        // e3: 주문 상태가 대기가 아닌 경우
        if (order.getStatus() != Status.WAIT) {
            throw new CustomException(ErrorCode.CHANGE_STATUS_ERROR, "주문 상태: " + order.getStatus());
        }

        // 주문 상태 변경
        order.updateStatus(Status.CANCELED);
        // 주문 삭제
        orderRepository.deleteById(orderId);

        return ChangeOrderStatusResponseDto.fromOrder(order);
    }

    private FoodStore findFoodStore(Long foodstoreId) {
        return foodStoreRepository.findById(foodstoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND,
                        "가게번호: " + foodstoreId));
    }

    private boolean isNotOperatingTime(LocalTime openAt, LocalTime closeAt) {
        LocalTime now = LocalTime.now();
        return !(now.isAfter(openAt) && now.isBefore(closeAt));
    }

    private void validFoodstoreOwner(Long foodstoreId, User user) {
        FoodStore foodStore = findFoodStore(foodstoreId);
        if (!user.getId().equals(foodStore.getUser().getId())) {
           throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER, "userId: " + user.getId());
        }

    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND,
                        "주문번호:" + orderId));
    }

    private void validOrderUser(User user, Order order) {
        if (!user.getId().equals(order.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_ORDER_USER_ID, "회원번호: " + user.getId());
        }
    }

    private void validDateTime(LocalDate startAt, LocalDate endAt) {
        // 시작 일자가 종료 일자보다 느린 경우
        if(startAt.isAfter(endAt)) {
            throw new CustomException(ErrorCode.DATE_BAD_REQUEST, "조회 시작일은 조회 종료일 이후일 수 없습니다.");
        }
        // 종료 일자가 오늘보다 미래인 경우
        if(endAt.isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.DATE_BAD_REQUEST, "조회 종료일은 오늘 이후일 수 없습니다.");
        }
    }
}
