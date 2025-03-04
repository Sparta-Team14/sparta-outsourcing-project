package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.category.request.MenuCategoryRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryDeletedBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.service.MenuCategoryService;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
public class MenuCategoryController {
    private final MenuCategoryService menuCategoryService;

    @PostMapping("/foodstore/{foodstoreId}/categories")
    private ResponseEntity<MenuCategoryResponseDto> createCategory(@RequestBody MenuCategoryRequestDto requestDto,
                                                                   @PathVariable Long foodstoreId,
                                                                   @Auth AuthUser authUser){
        log.info("{}",authUser.getId());
        log.info("{}",authUser.getEmail());
        log.info("{}",authUser.getUserRole());
        return new ResponseEntity<>(menuCategoryService.createCategory(authUser.getId(), foodstoreId,requestDto.getName()),HttpStatus.CREATED);
    }
    @PutMapping("/menu-categories/{categoryId}")
    private ResponseEntity<MenuCategoryResponseDto> updateCategory(@RequestBody MenuCategoryRequestDto requestDto,
                                                                   @PathVariable Long categoryId,
                                                                   @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.updateCategory(authUser.getId(),categoryId,requestDto.getName()));
    }
    @DeleteMapping("/menu-categories/{categoryId}")
    private ResponseEntity<MenuCategoryResponseDto> deleteCategory(@PathVariable Long categoryId,
                                                                   @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.deleteCategory(authUser.getId(), categoryId));
    }
    @PutMapping("/menu-categories/{categoryId}/restore")
    private ResponseEntity<MenuCategoryResponseDto> restoreCategory(@PathVariable Long categoryId,
                                                                    @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.restoreCategory(authUser.getId(), categoryId));
    }
    @GetMapping("/foodstores/{foodstoreId}/menu-categories")
    private ResponseEntity<List<MenuCategoryBasicDto>> findCategoryList(@PathVariable Long foodstoreId){
        return ResponseEntity.ok(menuCategoryService.findCategoryList(foodstoreId));
    }
    @GetMapping("/foodstores/{foodstoreId}/menu-categories/deleted")
    private ResponseEntity<List<MenuCategoryDeletedBasicDto>> findDeletedCategoryList(@PathVariable Long foodstoreId,
                                                                                      @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.findDeletedCategoryList(authUser.getId(), foodstoreId));
    }
}