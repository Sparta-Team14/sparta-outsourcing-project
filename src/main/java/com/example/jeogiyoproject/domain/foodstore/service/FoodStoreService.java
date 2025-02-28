package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreSaveRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodStoreService {
    private final FoodStoreRepository foodStoreRepository;

    @Transactional
    public FoodStoreResponseDto save(FoodStoreSaveRequestDto dto) {
        FoodStore foodStore = new FoodStore();

        foodStoreRepository.save(foodStore);
        return new FoodStoreResponseDto(
                foodStore.getId(),
                foodStore.getTitle(),
                foodStore.getAddress(),
                foodStore.getMinPrice(),
                foodStore.getOpenAt(),
                foodStore.getCloseAt(),
                foodStore.getCreatedAt(),
                foodStore.getUpdatedAt()
        );
    }
}
