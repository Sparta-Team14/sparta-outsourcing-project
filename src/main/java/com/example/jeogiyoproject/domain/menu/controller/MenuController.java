package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.menu.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuUpdateRequestDto;
import com.example.jeogiyoproject.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/menu-categories/{categoryId}/menus")
    private ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto,
                                                       @PathVariable Long categoryId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return new ResponseEntity<>(menuService.createMenu(userId,categoryId, requestDto), HttpStatus.CREATED);
    }
    @PutMapping("/menus/{menuId}")
    private ResponseEntity<MenuResponseDto> updateMenu(@RequestBody MenuUpdateRequestDto requestDto,
                                                       @PathVariable Long menuId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return ResponseEntity.ok(menuService.updateService(userId,menuId,requestDto));
    }
    @DeleteMapping("/menus/{menuId}")
    private ResponseEntity<MenuResponseDto> deleteMenu(@PathVariable Long menuId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return ResponseEntity.ok(menuService.deleteMenu(userId,menuId));
    }
}
