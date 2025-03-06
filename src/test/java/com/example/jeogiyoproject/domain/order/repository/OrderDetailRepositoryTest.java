package com.example.jeogiyoproject.domain.order.repository;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.order.entity.Order;
import com.example.jeogiyoproject.domain.order.entity.OrderDetail;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodStoreRepository foodStoreRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Test
    void 주문_번호로_주문_상세_목록을_조회할_수_있다() {

        User user = new User(
                "email",
                "password",
                "name",
                "address",
                UserRole.USER
        );
        User savedUser = userRepository.save(user);

        // given
        FoodStore foodstore = new FoodStore(
                savedUser,
                "title",
                "store_address",
                1,
                LocalTime.of(9, 0),
                LocalTime.of(21, 0)
        );
        FoodStore savedFoodStore = foodStoreRepository.save(foodstore);

        Order order = new Order(savedFoodStore,
                savedUser,
                1,
                1,
                "request"
        );
        Order savedOrder = orderRepository.save(order);

        MenuCategory menuCategory = new MenuCategory("category", foodstore);
        MenuCategory savedMenuCategory = menuCategoryRepository.save(menuCategory);

        Menu menu = new Menu(savedMenuCategory, "name", "info", 1);
        Menu savedMenu = menuRepository.save(menu);

        OrderDetail orderDetail = new OrderDetail(savedOrder, savedMenu, 1);
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

        // when
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(savedOrder.getId());

        // then
        assertNotNull(orderDetailList);
        assertEquals(orderDetailList.get(0).getOrder(), savedOrder);
        assertEquals(orderDetailList.get(0).getId(), savedOrderDetail.getId());
    }
}
