package com.example.jeogiyoproject.domain.foodstore.controller;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreDeleteRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreSearchResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreUpdateResponseDto;
import com.example.jeogiyoproject.domain.foodstore.service.FoodStoreService;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.global.common.annotation.Auth;
import com.example.jeogiyoproject.global.common.dto.AuthUser;
import com.example.jeogiyoproject.web.aop.annotation.AuthCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/foodstores")
public class FoodStoreController {
    private final FoodStoreService foodStoreService;

    // 가게생성
    @AuthCheck(UserRole.OWNER)
    @PostMapping
    public ResponseEntity<FoodStoreResponseDto> create(
            @RequestParam("title") String title,
            @RequestParam("address") String address,
            @RequestParam("minPrice") Integer minPrice,
            @RequestParam("openAt") LocalTime openAt,
            @RequestParam("closeAt") LocalTime closeAt,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @Auth AuthUser authUser
    ) {
        FoodStoreRequestDto dto = new FoodStoreRequestDto(
                title,
                address,
                minPrice,
                openAt,
                closeAt,
                image
        );
        return new ResponseEntity<>(foodStoreService.create(dto, authUser.getId()), HttpStatus.CREATED);
    }

    // 가게수정
    @PatchMapping("/{foodStoreId}")
    public ResponseEntity<FoodStoreUpdateResponseDto> update(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "openAt", required = false) LocalTime openAt,
            @RequestParam(value = "closeAt", required = false) LocalTime closeAt,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @PathVariable Long foodStoreId
    ) {
        FoodStoreRequestDto dto = new FoodStoreRequestDto(
                title,
                address,
                minPrice,
                openAt,
                closeAt,
                image
        );

        FoodStoreUpdateResponseDto responseDto = foodStoreService.update(foodStoreId, dto);
        return ResponseEntity.ok(responseDto);
    }

    // 가게 이름으로 조회
    @GetMapping("/search")
    public ResponseEntity<List<FoodStoreResponseDto>> findStoreByTitle(@RequestParam String title) {
        return ResponseEntity.ok(foodStoreService.findStoreByTitle(title));
    }

    // 가게 단건조회
    @GetMapping("/{foodStoreId}")
    public ResponseEntity<FoodStoreSearchResponseDto> getFoodStore(@PathVariable Long foodStoreId) {
        return ResponseEntity.ok(foodStoreService.getFoodStore(foodStoreId));
    }

    // 가게 폐업
    @AuthCheck(UserRole.OWNER)
    @DeleteMapping("/{foodStoreId}")
    public ResponseEntity<String> delete(
            @PathVariable Long foodStoreId,
            @Auth AuthUser authUser,
            @RequestBody FoodStoreDeleteRequestDto dto
    ) {
        foodStoreService.delete(foodStoreId, authUser.getId(), dto.getPassword());
        return ResponseEntity.ok("성공적으로 삭제되었습니다");
    }
}
