package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuCategoryRepository categoryRepository;
    @InjectMocks
    private MenuService menuService;

    @Test
    void Menu를_생성한다() {
        // given
//        private Long categoryId;
//        private String name;
//        private String info;
//        private Integer price;
        // when
        // then
    }

    @Test
    void updateMenu() {
    }

    @Test
    void deleteMenu() {
    }

    @Test
    void restoreMenu() {
    }

    @Test
    void findMenu() {
    }

    @Test
    void findMenuList() {
    }

    @Test
    void findDeletedMenuList() {
    }
}