package com.example.jeogiyoproject.domain.cart.repository;

import com.example.jeogiyoproject.domain.cart.entity.Cart;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodStoreRepository foodStoreRepository;

    @Test
    void 유저번호_기준_장바구니_조회 () {
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

        Cart cart = new Cart(user, foodStore);
        cartRepository.save(cart);

        // when
        Optional<Cart> findCart = cartRepository.findByUserId(savedUser.getId());

        // then
        assertTrue(findCart.isPresent());
        assertEquals(findCart.get().getFoodstore().getId(), savedFoodStore.getId());
        assertEquals(findCart.get().getUser().getId(), savedUser.getId());
    }

    @Test
    void 만료기간이_남은_장바구니는_조회되지_않음 () {
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

        Cart cart = new Cart(user, foodStore);
        cartRepository.save(cart);

        // when
        List<Cart> findExpiredCarts = cartRepository.findByUpdatedAtBefore(LocalDateTime.now().minusDays(1));

        // then
        assertEquals(0, findExpiredCarts.size());
    }

    @Test
    void 만료_장바구니_조회 () {
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

        Cart cart = new Cart(user, foodStore);
        cartRepository.save(cart);

        // when
        List<Cart> findExpiredCarts = cartRepository.findByUpdatedAtBefore(LocalDateTime.now().plusDays(1));

        // then
        assertEquals(1, findExpiredCarts.size());
    }
}
