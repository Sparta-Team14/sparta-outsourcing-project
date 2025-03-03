package com.example.jeogiyoproject.domain.menu.service;

import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryDeletedBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.exception.NotOwnerException;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final FoodStoreRepository foodStoreRepository;

    @Transactional
    public MenuCategoryResponseDto createCategory(Long userId, Long foodstoreId, String name) {
        // request의 foodstore를 조회하는 로직 (추후 db에서 조회하도록 변경)
        FoodStore foodStore = foodStoreRepository.findById(foodstoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOODSTORE_OWNER));
// foodstore Entity에 userId 추가되면 수정 예정
//        if(!foodStore.getUser().getId().equals(userId)){
//            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
//        }

        Long foodStoreOwnerId = 1L;
        if (!foodStoreOwnerId.equals(userId)) {
            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
        }
        MenuCategory category = new MenuCategory(name, foodStore);
        menuCategoryRepository.save(category);
        return MenuCategoryResponseDto.fromMenuCategory(category);
    }

    @Transactional
    public MenuCategoryResponseDto updateCategory(Long userId, Long categoryId, String name) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//        foodstore Entity에 userId 추가되면 수정 예정
//        if(!category.getFoodStore().getUser().getId().equals(userId)){
//            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
//        }

        Long foodStoreOwnerId = 1L;
        if (!foodStoreOwnerId.equals(userId)) {
            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
        }

        category.updateName(name);
        menuCategoryRepository.flush();
        return MenuCategoryResponseDto.fromMenuCategory(category);
    }

    @Transactional
    public MenuCategoryResponseDto deleteCategory(Long userId, Long categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//        foodstore Entity에 userId 추가되면 수정 예정
//        if(!category.getFoodStore().getUser().getId().equals(userId)){
//            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
//        }

        Long foodStoreOwnerId = 1L;
        if (!foodStoreOwnerId.equals(userId)) {
            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
        }
        category.setDeletedAt();
        menuCategoryRepository.flush();
        return MenuCategoryResponseDto.fromMenuCategory(category);
    }

    @Transactional
    public MenuCategoryResponseDto restoreCategory(Long userId, Long categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//        foodstore Entity에 userId 추가되면 수정 예정
//        if(!category.getFoodStore().getUser().getId().equals(userId)){
//            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
//        }

        Long foodStoreOwnerId = 1L;
        if (!foodStoreOwnerId.equals(userId)) {
            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
        }
        category.restore();
        menuCategoryRepository.flush();
        return MenuCategoryResponseDto.fromMenuCategory(category);
    }

    public List<MenuCategoryBasicDto> findCategoryList(Long foodstoreId) {
        List<MenuCategory> categories = menuCategoryRepository.findAllByFoodStoreIdAndDeletedAtIsNull(foodstoreId);
        return categories.stream()
                .map(category -> new MenuCategoryBasicDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public List<MenuCategoryDeletedBasicDto> findDeletedCategoryList(Long userId, Long foodstoreId) {
        // request의 foodstore를 조회하는 로직 (추후 db에서 조회하도록 변경)
        FoodStore foodStore = foodStoreRepository.findById(foodstoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOODSTORE_OWNER));
        // foodstore Entity에 userId 추가되면 수정 예정
//        if(!foodStore.getUser().getId().equals(userId)){
//            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
//        }

        Long foodStoreOwnerId = 1L;
        if (!foodStoreOwnerId.equals(userId)) {
            throw new NotOwnerException("해당 가게의 사장만 접근 가능합니다.");
        }
        List<MenuCategory> categories = menuCategoryRepository.findAllByFoodStoreIdAndDeletedAtIsNotNull(foodstoreId);
        return categories.stream()
                .map(category -> new MenuCategoryDeletedBasicDto(category.getId(), category.getName(),category.getDeletedAt()))
                .collect(Collectors.toList());
    }
}
