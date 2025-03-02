package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.menu.dto.category.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.exception.NotOwnerException;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;

//    public MenuCategoryResponseDto createCategory(Long userId, Long foodstoreId, String name) {
//        // request의 foodstore를 조회하는 로직 (추후 db에서 조회하도록 변경)
//        FoodStore store = new FoodStore(foodstoreId,"테스트 치킨집","호원동",10000,LocalDateTime.now(),LocalDateTime.now(),1L);
//
//        if(!store.getUserId().equals(userId)){
//            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
//        }
//        MenuCategory category = new MenuCategory(name, store);
//        menuCategoryRepository.save(category);
//        return MenuCategoryResponseDto.fromEntity(category);
//    }
}
