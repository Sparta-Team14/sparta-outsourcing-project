package com.example.jeogiyoproject.domain.cart.repository;

import com.example.jeogiyoproject.domain.cart.entity.Cart;
import com.example.jeogiyoproject.domain.cart.entity.CartItems;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CartItemsRepositoryTest {

    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodStoreRepository foodStoreRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Test
    void 장바구니_번호로_장바구니_품목을_조회할_수_있다() {
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

        MenuCategory menuCategory = new MenuCategory("category", savedFoodStore);
        MenuCategory savedMenuCategory = menuCategoryRepository.save(menuCategory);

        Cart cart = new Cart(user, foodStore);
        cartRepository.save(cart);

        List<Menu> menuList = new ArrayList<>();
        List<CartItems> cartItemsList = new ArrayList<>();
        int size = (int) (Math.random() * 10);
        for (int i = 0; i < size; i++) {
            Menu menu = new Menu(savedMenuCategory, "name" + i, "info", i);
            menuList.add(menu);
            cartItemsList.add(new CartItems(cart, menu, i));
        }
        menuRepository.saveAll(menuList);
        cartItemsRepository.saveAll(cartItemsList);

        // when
        List<CartItems> findCartItems = cartItemsRepository.findByCartId(cart.getId());

        // then
        assertNotNull(findCartItems);
        assertEquals(size, findCartItems.size());
    }

    @Test
    void 장바구니_번호와_메뉴번호로_장바구니_품목_단건_조회() {
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

        MenuCategory menuCategory = new MenuCategory("category", savedFoodStore);
        MenuCategory savedMenuCategory = menuCategoryRepository.save(menuCategory);

        Cart cart = new Cart(user, foodStore);
        cartRepository.save(cart);

        List<Menu> menuList = new ArrayList<>();
        List<CartItems> cartItemsList = new ArrayList<>();

        int size = 5;
        for (int i = 0; i < size; i++) {
            Menu menu = new Menu(savedMenuCategory, "name" + i, "info", i);
            menuList.add(menu);
            cartItemsList.add(new CartItems(cart, menu, i));
        }
        menuRepository.saveAll(menuList);
        cartItemsRepository.saveAll(cartItemsList);

        // when
        Optional<CartItems> findCartItem = cartItemsRepository.findByCartIdAndMenuId(cart.getId(), menuList.get(0).getId());

        // then
        assertTrue(findCartItem.isPresent());
        assertEquals(menuList.get(0).getId(), findCartItem.get().getMenu().getId());
    }

    @Test
    void 장바구니_번호_목록으로_장바구니_품목을_일괄_조회할_수_있다() {
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

        MenuCategory menuCategory = new MenuCategory("category", savedFoodStore);
        MenuCategory savedMenuCategory = menuCategoryRepository.save(menuCategory);

        Cart cart = new Cart(user, foodStore);
        cartRepository.save(cart);

        List<Menu> menuList = new ArrayList<>();
        List<CartItems> cartItemsList = new ArrayList<>();
        int size = (int) (Math.random() * 5) + 1;
        for (int i = 0; i < size; i++) {
            Menu menu = new Menu(savedMenuCategory, "name" + i, "info", i);
            menuList.add(menu);
            cartItemsList.add(new CartItems(cart, menu, i));
        }
        menuRepository.saveAll(menuList);
        cartItemsRepository.saveAll(cartItemsList);

        List<Long> cartIds = new ArrayList<>();
        cartIds.add(cart.getId());

        // when
        List<CartItems> findCartItems = cartItemsRepository.findByCartIdIn(cartIds);

        // then
        assertNotNull(findCartItems);
        assertEquals(size, findCartItems.size());
    }

}
