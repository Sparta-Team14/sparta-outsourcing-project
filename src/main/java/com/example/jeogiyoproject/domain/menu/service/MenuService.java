package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuUpdateRequestDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
        Long foodstoreOwnerId = 1L;
        if(!foodstoreOwnerId.equals(userId)){
            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
        }

//        boolean nameExist = menuCategoryRepository.existsByFoodStoreIdAndName(foodstoreId, name);
//        if(nameExist){
//            throw new CustomException(ErrorCode.CATEGORY_IS_EXIST);
//        }
        boolean nameExist = menuRepository.existsByMenuCategory_FoodStore_IdAndName(category.getFoodStore().getId(),requestDto.getName());
        if(nameExist){
            throw new CustomException(ErrorCode.MENU_IS_EXIST);
        }

        Menu menu = new Menu(category,requestDto.getName(),requestDto.getInfo(),requestDto.getPrice());
        menuRepository.save(menu);
        return MenuResponseDto.fromMenu(menu);
    }
    @Transactional
    public MenuResponseDto updateMenu(Long userId, Long menuId, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));
//        Long foodStoreOwnerId = menu.getMenuCategory().getFoodStore().getUser().getUserId();

        Long foodStoreOwnerId = 1L;
        Long foodstoreId = menu.getMenuCategory().getFoodStore().getId();
        if(!foodStoreOwnerId.equals(userId)){
            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
        }

        boolean nameExist = menuRepository.existsByMenuCategory_FoodStore_IdAndName(foodstoreId,requestDto.getName());
        if(nameExist){
            throw new CustomException(ErrorCode.MENU_IS_EXIST);
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

        if(menu.getDeletedAt() != null){
            throw new CustomException(ErrorCode.MENU_ALREADY_DELETED);
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

        if(menu.getDeletedAt() == null){
            throw new CustomException(ErrorCode.MENU_NOT_DELETED);
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

    @Transactional(readOnly = true)
    public List<MenuCategoryListResponseDto> findMenuList(Long foodstoreId) {
        List<MenuCategoryListResponseDto> categories = menuRepository.findCategoriesAndMenusByFoodStoreId(foodstoreId);

        for (MenuCategoryListResponseDto categoryDto : categories) {
            List<Menu> menus = menuRepository.findMenusByMenuCategoryIdAndDeletedAtIsNull(categoryDto.getCategoryId());
            for (Menu menu : menus) {
                MenuBasicDto menuDto = new MenuBasicDto(menu.getId(), menu.getName(), menu.getInfo(), menu.getPrice());
                categoryDto.addMenu(menuDto);
            }
        }
        return categories;
    }
    @Transactional(readOnly = true)
    public List<MenuCategoryListResponseDto> findDeletedMenuList(Long foodstoreId) {
        List<MenuCategoryListResponseDto> categories = menuRepository.findCategoriesByFoodStoreId(foodstoreId);

        for (MenuCategoryListResponseDto categoryDto : categories) {
            List<Menu> menus = menuRepository.findMenusByMenuCategoryIdAndDeletedAtIsNotNull(categoryDto.getCategoryId());
            for (Menu menu : menus) {
                MenuBasicDto menuDto = new MenuBasicDto(menu.getId(), menu.getName(), menu.getInfo(), menu.getPrice());
                categoryDto.addMenu(menuDto);
            }
        }
        return categories;
    }
}
