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

    /***
     * 메뉴 생성
     * @param requestDto 카테고리 ID, 메뉴이름, 메뉴 정보, 가격을 포함하는 DTO
     * @param authUser JWT 토큰에서 유저 반환
     * @return 생성된 메뉴 정보를 리턴
     */
    @PostMapping
    private ResponseEntity<MenuResponseDto> createMenu(@RequestBody MenuRequestDto requestDto,
                                                       @Auth AuthUser authUser){
        return new ResponseEntity<>(menuService.createMenu(authUser.getId(), requestDto), HttpStatus.CREATED);
    }

    /***
     * 메뉴 업데이트
     * @param requestDto 업데이트할 메뉴 이름, 정보, 가격을 포함하는 DTO
     * @param menuId 업데이트할 메뉴의 ID
     * @param authUser JWT 토큰에서 유저 정보 반환
     * @return 업데이트된 메뉴 정보 리턴
     */
    @PutMapping("/{menuId}")
    private ResponseEntity<MenuResponseDto> updateMenu(@RequestBody MenuUpdateRequestDto requestDto,
                                                       @PathVariable Long menuId,
                                                       @Auth AuthUser authUser){
        return ResponseEntity.ok(menuService.updateMenu(authUser.getId(), menuId,requestDto));
    }

    /***
     * 메뉴 삭제
     * @param menuId 삭제할 메뉴의 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 삭제된 메뉴의 정보 반환
     */
    @DeleteMapping("/{menuId}")
    private ResponseEntity<MenuResponseDto> deleteMenu(@PathVariable Long menuId,
                                                       @Auth AuthUser authUser){
        return ResponseEntity.ok(menuService.deleteMenu(authUser.getId(), menuId));
    }

    /***
     * 삭제된 메뉴 복구
     * @param menuId 복구할 메뉴의 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 복구된 메뉴의 정보 반환
     */
    @PutMapping("/{menuId}/restore")
    private ResponseEntity<MenuResponseDto> restoreMenu(@PathVariable Long menuId,
                                                        @Auth AuthUser authUser){
        return ResponseEntity.ok(menuService.restoreMenu(authUser.getId(), menuId));
    }

    /***
     * 메뉴 단건조회
     * @param menuId 조회할 메뉴의 ID
     * @return 조회된 메뉴의 상세정보 반환
     */
    @GetMapping("/{menuId}")
    private ResponseEntity<MenuResponseDto> findMenu(@PathVariable Long menuId){
        return ResponseEntity.ok(menuService.findMenu(menuId));
    }

    /***
     * 해당 가게의 메뉴리스트 조회
     * @param requestDto 가게의 ID를 포함하는 DTO
     * @return 해당 가게의 메뉴리스트 반환
     */
    @GetMapping
    private ResponseEntity<List<MenuCategoryListResponseDto>> findMenuList(@RequestBody MenuListRequestDto requestDto){
        return ResponseEntity.ok(menuService.findMenuList(requestDto.getFoodstoreId()));
    }

    /***
     * 해당 가게의 삭제된 메뉴리스트 조회
     * @param requestDto 가게의 ID를 포함하는 DTO
     * @return 해당 가게의 삭제된 메뉴리스트 반환
     */
    @GetMapping("/deleted")
    private ResponseEntity<List<MenuCategoryListResponseDto>> findDeletedMenuList(@RequestBody MenuListRequestDto requestDto){
        return ResponseEntity.ok(menuService.findDeletedMenuList(requestDto.getFoodstoreId()));
    }
}
