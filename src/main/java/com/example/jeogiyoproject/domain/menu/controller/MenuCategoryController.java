package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.category.request.MenuCategoryRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryDeletedBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.service.MenuCategoryService;
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
                                                                   @PathVariable Long foodstoreId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        log.info("userId = {}",userId);
        return new ResponseEntity<>(menuCategoryService.createCategory(userId,foodstoreId,requestDto.getName()),HttpStatus.CREATED);
    }
    @PutMapping("/menu-categories/{categoryId}")
    private ResponseEntity<MenuCategoryResponseDto> updateCategory(@RequestBody MenuCategoryRequestDto requestDto,
                                                                   @PathVariable Long categoryId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return ResponseEntity.ok(menuCategoryService.updateCategory(userId,categoryId,requestDto.getName()));
    }
    @DeleteMapping("/menu-categories/{categoryId}")
    private ResponseEntity<MenuCategoryResponseDto> deleteCategory(@PathVariable Long categoryId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return ResponseEntity.ok(menuCategoryService.deleteCategory(userId,categoryId));
    }
    @PutMapping("/menu-categories/{categoryId}/restore")
    private ResponseEntity<MenuCategoryResponseDto> restoreCategory(@PathVariable Long categoryId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return ResponseEntity.ok(menuCategoryService.restoreCategory(userId,categoryId));
    }
    @GetMapping("/foodstores/{foodstoreId}/menu-categories")
    private ResponseEntity<List<MenuCategoryBasicDto>> findCategoryList(@PathVariable Long foodstoreId){
        return ResponseEntity.ok(menuCategoryService.findCategoryList(foodstoreId));
    }

    @GetMapping("/foodstores/{foodstoreId}/menu-categories/deleted")
    private ResponseEntity<List<MenuCategoryDeletedBasicDto>> findDeletedCategoryList(@PathVariable Long foodstoreId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return ResponseEntity.ok(menuCategoryService.findDeletedCategoryList(userId, foodstoreId));
    }
}
