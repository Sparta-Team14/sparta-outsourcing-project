package com.example.jeogiyoproject.domain.foodstore.controller;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreDeleteRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreSearchResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreUpdateResponseDto;
import com.example.jeogiyoproject.domain.foodstore.service.FoodStoreService;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FoodStoreController {
    private final FoodStoreService foodStoreService;

    // 가게생성
    @PostMapping("/foodstores/users/{userId}")
    public ResponseEntity<FoodStoreResponseDto> create(
            @RequestBody FoodStoreRequestDto dto,
            @PathVariable Long userId,
            @Auth AuthUser authUser
    ) {
        return ResponseEntity.ok(foodStoreService.create(dto,userId));
    }

    // 가게수정
    @PatchMapping("/foodstores/{foodStoreId}")
    public ResponseEntity<FoodStoreUpdateResponseDto> update(
            @PathVariable Long foodStoreId,
            @RequestBody FoodStoreRequestDto dto) {
        FoodStoreUpdateResponseDto responseDto = foodStoreService.update(foodStoreId, dto);
        return ResponseEntity.ok(responseDto);
    }

    // 가게 이름으로 조회
    @GetMapping("/foodstores/search")
    public ResponseEntity<List<FoodStoreResponseDto>> findStoreByTitle(@RequestParam String title) {
        return ResponseEntity.ok(foodStoreService.findStoreByTitle(title));
    }

    // 가게 단건조회
    @GetMapping("/foodstores/{foodStoreId}")
    public ResponseEntity<FoodStoreSearchResponseDto> getFoodStore(@PathVariable Long foodStoreId) {
        return ResponseEntity.ok(foodStoreService.getFoodStore(foodStoreId));
    }

    // 가게 폐업
    @DeleteMapping("/foodstores/{foodStoreId}/users/{userId}")
    public ResponseEntity<String> delete(
            @PathVariable Long foodStoreId,
            @PathVariable Long userId,
            @Auth AuthUser authUser,
            @RequestBody FoodStoreDeleteRequestDto dto
    ) {
        foodStoreService.delete(foodStoreId, userId, dto.getPassword());
        return ResponseEntity.ok("성공적으로 삭제되었습니다");
    }
}
