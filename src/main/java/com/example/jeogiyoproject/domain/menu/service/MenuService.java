package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.menu.dto.menu.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCategoryRepository categoryRepository;

    public MenuResponseDto createMenu(Long userId, Long categoryId, MenuRequestDto requestDto) {
        MenuCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        // category.getFoodStore().getUserId() 와 userId를 비교, 해당 가게의 사장인지 확인
        Menu menu = new Menu(category,requestDto.getName(),requestDto.getInfo(),requestDto.getPrice());
        menuRepository.save(menu);
        return MenuResponseDto.fromMenu(menu);
    }
}
