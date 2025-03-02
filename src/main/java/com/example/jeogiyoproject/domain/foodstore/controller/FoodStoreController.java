package com.example.jeogiyoproject.domain.foodstore.controller;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreSaveRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.service.FoodStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FoodStoreController {
    private final FoodStoreService foodStoreService;

    @PostMapping("/foodstores")
    public ResponseEntity<FoodStoreResponseDto> save(@RequestBody FoodStoreSaveRequestDto dto) {
        return ResponseEntity.ok(foodStoreService.save(dto));
    }
}
