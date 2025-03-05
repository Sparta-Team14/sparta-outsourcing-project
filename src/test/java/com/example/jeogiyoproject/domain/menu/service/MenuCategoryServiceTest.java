package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuCategoryServiceTest {
    @Mock
    private MenuCategoryRepository menuCategoryRepository;
    @Mock
    private FoodStoreRepository foodStoreRepository;
    @InjectMocks
    private MenuCategoryService menuCategoryService;

    @Test
    void MENU_CATEGORY를_생성한다() {
        // given
        Long userId = 1L;
        Long foodStoreId = 1L;
        String name = "주 메뉴";

        FoodStore foodStore = new FoodStore();
        ReflectionTestUtils.setField(foodStore,"id",foodStoreId);
        given(foodStoreRepository.findById(foodStoreId)).willReturn(Optional.of(foodStore));

        // when
        MenuCategoryResponseDto responseDto = menuCategoryService.createCategory(userId,foodStoreId,name);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getName()).isEqualTo(name);
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }

    @Test
    void restoreCategory() {
    }

    @Test
    void findCategoryList() {
    }

    @Test
    void findDeletedCategoryList() {
    }
}