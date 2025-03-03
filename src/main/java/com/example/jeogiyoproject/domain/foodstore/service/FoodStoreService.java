package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreDeleteRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreSearchResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreUpdateResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodStoreService {
    private final FoodStoreRepository foodStoreRepository;

    //가게 생성
    @Transactional
    public FoodStoreResponseDto save(FoodStoreRequestDto dto) {
        FoodStore foodStore = new FoodStore(
                dto.getTitle(),
                dto.getAddress(),
                dto.getMinPrice(),
                dto.getOpenAt(),
                dto.getCloseAt()
        );

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

    // 가게 이름검색
    @Transactional(readOnly = true)
    public List<FoodStoreResponseDto> findStoreByTitle(String title) {
        List<FoodStore> foodStores = foodStoreRepository.findByTitleContaining(title);
        List<FoodStoreResponseDto> responseDto = new ArrayList<>();

        for (FoodStore foodStore : foodStores) {
            FoodStoreResponseDto dto = new FoodStoreResponseDto(
                    foodStore.getId(),
                    foodStore.getTitle(),
                    foodStore.getAddress(),
                    foodStore.getMinPrice(),
                    foodStore.getOpenAt(),
                    foodStore.getCloseAt(),
                    foodStore.getCreatedAt(),
                    foodStore.getUpdatedAt()
            );
            responseDto.add(dto);
        }

        return responseDto;
    }

    // 가게 단건조회
    @Transactional(readOnly = true)
    public FoodStoreSearchResponseDto getFoodStore(Long foodStoreId) {
        FoodStore foodStore = foodStoreRepository.findById(foodStoreId).orElseThrow(() -> new IllegalArgumentException("Invalid foodStoreId"));

        return new FoodStoreSearchResponseDto(
                foodStore.getId(),
                foodStore.getTitle(),
                foodStore.getAddress(),
                foodStore.getMinPrice(),
                foodStore.getOpenAt(),
                foodStore.getCloseAt()
        );
    }

    // 가게 정보수정
    @Transactional
    public FoodStoreUpdateResponseDto update(Long foodStoreId, FoodStoreRequestDto dto) {
        FoodStore foodStore = foodStoreRepository.findById(foodStoreId).orElseThrow(
                () -> new IllegalArgumentException("해당 id에 가게가 없습니다.")
        );

        foodStore.update(
                dto.getTitle(),
                dto.getAddress(),
                dto.getMinPrice(),
                dto.getOpenAt(),
                dto.getCloseAt()
        );

        return new FoodStoreUpdateResponseDto(
                foodStore.getId(),
                foodStore.getTitle(),
                foodStore.getAddress(),
                foodStore.getMinPrice(),
                foodStore.getOpenAt(),
                foodStore.getCloseAt(),
                foodStore.getUpdatedAt()
        );
    }

    // 가게삭제
    @Transactional
    public void delete(FoodStoreDeleteRequestDto dto) {
        FoodStore foodStore = foodStoreRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        foodStore.setDeletedAt(LocalDateTime.now());

        foodStoreRepository.save(foodStore);
    }
}
