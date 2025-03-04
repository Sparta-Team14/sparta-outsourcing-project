package com.example.jeogiyoproject.domain.foodstore.controller;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreDeleteRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreSearchResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreUpdateResponseDto;
import com.example.jeogiyoproject.domain.foodstore.service.FoodStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FoodStoreController {
    private final FoodStoreService foodStoreService;

    // 가게생성
    @PostMapping("/foodstores")
    public ResponseEntity<FoodStoreResponseDto> create(
            @RequestBody FoodStoreRequestDto dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        return ResponseEntity.ok(foodStoreService.create(dto,token));
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
    @DeleteMapping("/foodstores/{foodStoreId}")
    public ResponseEntity<String> delete(
            @PathVariable Long foodStoreId,
            @RequestHeader("Authorization") String token,
            @RequestBody FoodStoreDeleteRequestDto dto
    ) {
        foodStoreService.delete(foodStoreId, token, dto.getPassword());
        return ResponseEntity.ok("성공적으로 삭제되었습니다");
    }
}
