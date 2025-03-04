package com.example.jeogiyoproject.domain.order.repository;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.enums.Status;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodStoreRepository foodStoreRepository;


    @Test
    void 가게별_주문_목록을_생성일시_내림차순으로_조회할_수_있다() {
        Pageable pageable = PageRequest.of(0, 10);
        // given
        User user = new User(
                "email",
                "password",
                "name",
                "address",
                UserRole.USER
        );
        User savedUser = userRepository.save(user);

        FoodStore foodStore = new FoodStore(
                savedUser,
                "title",
                "store_address",
                1,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        );
        FoodStore savedFoodStore = foodStoreRepository.save(foodStore);
        long foodstoreId = savedFoodStore.getId();

        Order order = new Order(savedFoodStore,
                savedUser,
                1,
                1,
                "request"
        );
        orderRepository.save(order);

        // when
        Page<Order> findOrders = orderRepository.findAllByFoodstoreIdByCreatedAtDesc(pageable, foodstoreId, Status.WAIT, null, null);

        // then
        assertNotNull(findOrders);
        assertEquals(findOrders.getTotalElements(), 1);
    }

    @Test
    void 회원_번호로_회원의_주문_목록을_조회할_수_있다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User(
                "email",
                "password",
                "name",
                "address",
                UserRole.USER
        );
        User savedUser = userRepository.save(user);

        FoodStore foodStore = new FoodStore(
                savedUser,
                "title",
                "store_address",
                1,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        );
        FoodStore savedFoodStore = foodStoreRepository.save(foodStore);

        Order order = new Order(savedFoodStore,
                savedUser,
                1,
                1,
                "request"
        );
        orderRepository.save(order);

        // when
        Page<Order> orders = orderRepository.findAllByUserId(pageable, savedUser.getId(), null, Status.WAIT, null, null);

        // then
        assertNotNull(orders);
        assertEquals(orders.getTotalElements(), 1);
    }
}
