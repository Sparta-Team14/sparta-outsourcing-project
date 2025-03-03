package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.menu.dto.menu.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuUpdateRequestDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCategoryRepository categoryRepository;

    @Transactional
    public MenuResponseDto createMenu(Long userId, MenuRequestDto requestDto) {
        MenuCategory category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        // category.getFoodStore().getUserId() 와 userId를 비교, 해당 가게의 사장인지 확인
        Menu menu = new Menu(category,requestDto.getName(),requestDto.getInfo(),requestDto.getPrice());
        menuRepository.save(menu);
        return MenuResponseDto.fromMenu(menu);
    }
    @Transactional
    public MenuResponseDto updateService(Long userId, Long menuId, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
//        Long ownerId = menu.getMenuCategory().getFoodStore().getUser().getUserId();
        Long ownerId = 1L;
        if(!ownerId.equals(userId)){
            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
        }
        menu.updateMenu(requestDto);
        menuRepository.flush();
        return MenuResponseDto.fromMenu(menu);
    }
    @Transactional
    public MenuResponseDto deleteMenu(Long userId, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
        //        Long ownerId = menu.getMenuCategory().getFoodStore().getUser().getUserId();
        Long ownerId = 1L;
        if(!ownerId.equals(userId)){
            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
        }
        menu.setDeletedAt();
        menuRepository.flush();
        return MenuResponseDto.fromMenu(menu);
    }
    @Transactional
    public MenuResponseDto restoreMenu(Long userId, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
        //        Long ownerId = menu.getMenuCategory().getFoodStore().getUser().getUserId();
        Long ownerId = 1L;
        if(!ownerId.equals(userId)){
            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
        }
        menu.restore();
        menuRepository.flush();
        return MenuResponseDto.fromMenu(menu);
    }
    @Transactional(readOnly = true)
    public MenuResponseDto findMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        if(menu.getDeletedAt() != null){
            throw new CustomException(ErrorCode.MENU_DELETED);
        }

        return MenuResponseDto.fromMenu(menu);
    }
}
