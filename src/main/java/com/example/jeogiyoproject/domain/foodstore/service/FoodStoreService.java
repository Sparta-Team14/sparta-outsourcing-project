package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreSearchResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreUpdateResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuBasicDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.aws.S3Service;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodStoreService {
    private final FoodStoreRepository foodStoreRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    //가게 생성
    @Transactional
    public FoodStoreResponseDto create(FoodStoreRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_EXIST));

        boolean exists = foodStoreRepository.existsByTitleAndAddress(dto.getTitle(), dto.getAddress());
        if (exists) {
            throw new CustomException(ErrorCode.SAME_NAME_AND_ADDRESS);
        }

        long count = foodStoreRepository.countActiveStoresByUser(user);
        if (count >= 3) {
            throw new CustomException(ErrorCode.MAXIMUM_STORE);
        }

        String imgUrl = null;
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            try {
                imgUrl = s3Service.uploadImage(dto.getImage());
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        FoodStore foodStore = new FoodStore(
                user,
                dto.getTitle(),
                dto.getAddress(),
                dto.getMinPrice(),
                dto.getOpenAt(),
                dto.getCloseAt(),
                imgUrl
        );

        foodStoreRepository.save(foodStore);
        return new FoodStoreResponseDto(
                foodStore.getId(),
                user.getId(),
                foodStore.getTitle(),
                foodStore.getAddress(),
                foodStore.getMinPrice(),
                foodStore.getOpenAt(),
                foodStore.getCloseAt(),
                foodStore.getCreatedAt(),
                foodStore.getUpdatedAt(),
                foodStore.getImageUrl()
        );
    }

    // 가게 이름검색
    @Transactional(readOnly = true)
    public List<FoodStoreResponseDto> findStoreByTitle(String title) {
        List<FoodStore> foodStores = foodStoreRepository.findByTitleContaining(title);
        if (foodStores.isEmpty()) {
            throw new CustomException(ErrorCode.FOODSTORE_NOT_FOUND);
        }

        List<FoodStoreResponseDto> responseDto = new ArrayList<>();

        for (FoodStore foodStore : foodStores) {
            FoodStoreResponseDto dto = new FoodStoreResponseDto(
                    foodStore.getId(),
                    foodStore.getUserId(),
                    foodStore.getTitle(),
                    foodStore.getAddress(),
                    foodStore.getMinPrice(),
                    foodStore.getOpenAt(),
                    foodStore.getCloseAt(),
                    foodStore.getCreatedAt(),
                    foodStore.getUpdatedAt(),
                    foodStore.getImageUrl()
            );
            responseDto.add(dto);
        }

        return responseDto;
    }

    // 가게 단건조회
    @Transactional(readOnly = true)
    public FoodStoreSearchResponseDto getFoodStore(Long foodStoreId) {
        FoodStore foodStore = foodStoreRepository.findById(foodStoreId).orElseThrow(
                () -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND)
        );

        List<MenuCategoryListResponseDto> categories = menuRepository.findCategoriesAndMenusByFoodStoreId(foodStoreId);

        for (MenuCategoryListResponseDto categoryDto : categories) {
            List<Menu> menus = menuRepository.findMenusByMenuCategoryIdAndDeletedAtIsNull(categoryDto.getCategoryId());
            for (Menu menu : menus) {
                MenuBasicDto menuDto = new MenuBasicDto(menu.getId(), menu.getName(), menu.getInfo(), menu.getPrice());
                categoryDto.addMenu(menuDto);
            }
        }

        return new FoodStoreSearchResponseDto(
                foodStore.getId(),
                foodStore.getTitle(),
                foodStore.getAddress(),
                foodStore.getMinPrice(),
                foodStore.getOpenAt(),
                foodStore.getCloseAt(),
                foodStore.getImageUrl(),
                categories
        );
    }

    // 가게 정보수정
    @Transactional
    public FoodStoreUpdateResponseDto update(Long foodStoreId, FoodStoreRequestDto dto) {
        FoodStore foodStore = foodStoreRepository.findById(foodStoreId).orElseThrow(
                () -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND)
        );

        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imgUrl;
            try {
                imgUrl = s3Service.uploadImage(dto.getImage());
                foodStore.setImgUrl(imgUrl);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (dto.getTitle() != null) {
            foodStore.setTitle(dto.getTitle());
        }
        if (dto.getAddress() != null) {
            foodStore.setAddress(dto.getAddress());
        }
        if (dto.getMinPrice() != null) {
            foodStore.setMinPrice(dto.getMinPrice());
        }
        if (dto.getOpenAt() != null) {
            foodStore.setOpenAt(dto.getOpenAt());
        }
        if (dto.getCloseAt() != null) {
            foodStore.setCloseAt(dto.getCloseAt());
        }

        foodStoreRepository.save(foodStore);

        return new FoodStoreUpdateResponseDto(
                foodStore.getId(),
                foodStore.getTitle(),
                foodStore.getAddress(),
                foodStore.getMinPrice(),
                foodStore.getOpenAt(),
                foodStore.getCloseAt(),
                foodStore.getImageUrl(),
                foodStore.getUpdatedAt()
        );
    }

    // 가게삭제
    @Transactional
    public void delete(Long foodStoreId, Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_EXIST));

        // es.1 비밀번호가 일치하지 않을시
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }

        FoodStore foodStore = foodStoreRepository.findById(foodStoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND));

        if(!foodStore.getUser().getId().equals(userId)){
            throw new CustomException(ErrorCode.NOT_FOODSTORE_OWNER);
        }

        foodStore.setDeletedAt(LocalDateTime.now());
        foodStoreRepository.save(foodStore);
    }
}
