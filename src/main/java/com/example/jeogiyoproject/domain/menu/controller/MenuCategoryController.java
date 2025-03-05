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

    /***
     * 메뉴 카테고리 생성
     * @param requestDto 카테고리 이름을 포함하는 DTO
     * @param foodstoreId 카테고리를 생성할 가게의 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 생성된 카테고리의 정보 반환
     */
    @PostMapping("/foodstore/{foodstoreId}/categories")
    private ResponseEntity<MenuCategoryResponseDto> createCategory(@RequestBody MenuCategoryRequestDto requestDto,
                                                                   @PathVariable Long foodstoreId,
                                                                   @Auth AuthUser authUser){
        return new ResponseEntity<>(menuCategoryService.createCategory(authUser.getId(), foodstoreId,requestDto.getName()),HttpStatus.CREATED);
    }

    /***
     * 메뉴 카테고리 업데이트
     * @param requestDto 카테고리 이름을 포함하는 DTO
     * @param categoryId 업데이트할 카테고리 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 수정된 카테고리 정보 반환
     */
    @PutMapping("/menu-categories/{categoryId}")
    private ResponseEntity<MenuCategoryResponseDto> updateCategory(@RequestBody MenuCategoryRequestDto requestDto,
                                                                   @PathVariable Long categoryId,
                                                                   @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.updateCategory(authUser.getId(),categoryId,requestDto.getName()));
    }

    /***
     * 메뉴 카테고리 삭제
     * @param categoryId 삭제할 카테고리 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 삭제된 카테고리 정보 반환
     */
    @DeleteMapping("/menu-categories/{categoryId}")
    private ResponseEntity<MenuCategoryResponseDto> deleteCategory(@PathVariable Long categoryId,
                                                                   @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.deleteCategory(authUser.getId(), categoryId));
    }

    /***
     * 메뉴 카테고리 복구
     * @param categoryId 복구할 카테고리 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 복구한 카테고리 정보 반환
     */
    @PutMapping("/menu-categories/{categoryId}/restore")
    private ResponseEntity<MenuCategoryResponseDto> restoreCategory(@PathVariable Long categoryId,
                                                                    @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.restoreCategory(authUser.getId(), categoryId));
    }

    /***
     * 메뉴 카테고리 리스트 조회
     * @param foodstoreId 조회할 가게의 ID
     * @return 해당 가게의 카테고리 리스트 반환
     */
    @GetMapping("/foodstores/{foodstoreId}/menu-categories")
    private ResponseEntity<List<MenuCategoryBasicDto>> findCategoryList(@PathVariable Long foodstoreId){
        return ResponseEntity.ok(menuCategoryService.findCategoryList(foodstoreId));
    }

    /***
     * 삭제된 메뉴 카테고리 리스트 조회
     * @param foodstoreId 조회할 가게의 ID
     * @param authUser JWT 토큰에서 로그인 정보 반환
     * @return 해당 가게의 삭제된 카테고리 리스트 반환
     */
    @GetMapping("/foodstores/{foodstoreId}/menu-categories/deleted")
    private ResponseEntity<List<MenuCategoryDeletedBasicDto>> findDeletedCategoryList(@PathVariable Long foodstoreId,
                                                                                      @Auth AuthUser authUser){
        return ResponseEntity.ok(menuCategoryService.findDeletedCategoryList(authUser.getId(), foodstoreId));
    }
}