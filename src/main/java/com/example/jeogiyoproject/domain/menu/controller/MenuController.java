package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuListRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.request.MenuUpdateRequestDto;
import com.example.jeogiyoproject.domain.menu.service.MenuService;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    private ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto,
                                                       @Auth AuthUser authUser){
        return new ResponseEntity<>(menuService.createMenu(authUser.getId(), requestDto), HttpStatus.CREATED);
    }
    @PutMapping("/{menuId}")
    private ResponseEntity<MenuResponseDto> updateMenu(@RequestBody MenuUpdateRequestDto requestDto,
                                                       @PathVariable Long menuId,
                                                       @Auth AuthUser authUser){
        return ResponseEntity.ok(menuService.updateMenu(authUser.getId(), menuId,requestDto));
    }
    @DeleteMapping("/{menuId}")
    private ResponseEntity<MenuResponseDto> deleteMenu(@PathVariable Long menuId,
                                                       @Auth AuthUser authUser){
        return ResponseEntity.ok(menuService.deleteMenu(authUser.getId(), menuId));
    }
    @PutMapping("/{menuId}/restore")
    private ResponseEntity<MenuResponseDto> restoreMenu(@PathVariable Long menuId,
                                                        @Auth AuthUser authUser){
        return ResponseEntity.ok(menuService.restoreMenu(authUser.getId(), menuId));
    }
    @GetMapping("/{menuId}")
    private ResponseEntity<MenuResponseDto> findMenu(@PathVariable Long menuId){
        return ResponseEntity.ok(menuService.findMenu(menuId));
    }
    @GetMapping
    private ResponseEntity<List<MenuCategoryListResponseDto>> findMenuList(@RequestBody MenuListRequestDto requestDto){
        return ResponseEntity.ok(menuService.findMenuList(requestDto.getFoodstoreId()));
    }
    @GetMapping("/deleted")
    private ResponseEntity<List<MenuCategoryListResponseDto>> findDeletedMenuList(@RequestBody MenuListRequestDto requestDto){
        return ResponseEntity.ok(menuService.findDeletedMenuList(requestDto.getFoodstoreId()));
    }
}
