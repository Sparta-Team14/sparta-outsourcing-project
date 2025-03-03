package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.category.MenuCategoryRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.category.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.service.MenuCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuCategoryController {
    private final MenuCategoryService menuCategoryService;

    @PostMapping("/foodstore/{foodstoreId}/categories")
    private ResponseEntity<MenuCategoryResponseDto> createCategory(@RequestBody MenuCategoryRequestDto requestDto,
                                                                   @PathVariable Long foodstoreId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
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
}
