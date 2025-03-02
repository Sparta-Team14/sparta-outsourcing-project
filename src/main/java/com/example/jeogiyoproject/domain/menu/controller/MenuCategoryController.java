package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.category.MenuCategoryRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.category.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.service.MenuCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuCategoryController {
    private final MenuCategoryService menuCategoryService;

//    @PostMapping("/foodstore/{id}/categories")
//    private ResponseEntity<MenuCategoryResponseDto> createCategory(@RequestBody MenuCategoryRequestDto requestDto,
//                                                                   @PathVariable(name = "id") Long foodstoreId){
//        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
//        Long userId = 1L;
//        return new ResponseEntity<>(menuCategoryService.createCategory(userId,foodstoreId,requestDto.getName()),HttpStatus.CREATED);
//    }
}
