package com.example.jeogiyoproject.domain.menu.controller;

import com.example.jeogiyoproject.domain.menu.dto.menu.MenuRequestDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/menu-categories/{id}/menus")
    private ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto,
                                                       @PathVariable(name = "id") Long categoryId){
        // AOP로 userRole 체크, userId 가져오는 로직 추가 예정
        Long userId = 1L;
        return new ResponseEntity<>(menuService.createMenu(userId,categoryId, requestDto), HttpStatus.CREATED);
    }

}
