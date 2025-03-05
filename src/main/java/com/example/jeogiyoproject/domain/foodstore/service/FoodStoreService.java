package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreSearchResponseDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreUpdateResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryListResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.category.response.MenuCategoryResponseDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuBasicDto;
import com.example.jeogiyoproject.domain.menu.dto.menu.response.MenuResponseDto;
import com.example.jeogiyoproject.domain.menu.entity.Menu;
import com.example.jeogiyoproject.domain.menu.entity.MenuCategory;
import com.example.jeogiyoproject.domain.menu.repository.MenuCategoryRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.menu.service.MenuService;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import com.example.jeogiyoproject.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodStoreService {
    private final FoodStoreRepository foodStoreRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    //가게 생성
    @Transactional
    public FoodStoreResponseDto create(FoodStoreRequestDto dto, String token) {
        String jwt = jwtUtil.substringToken(token);
        Long userId = Long.valueOf(jwtUtil.extractClaims(jwt).getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_EXIST));

        // es.1 유저 role이 OWNER가 아닐때 생성불가
        if (!UserRole.OWNER.equals(user.getRole())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        // es.2 이름과 주소가 동일하면 생성불가
        boolean exists = foodStoreRepository.existsByTitleAndAddress(dto.getTitle(), dto.getAddress());
        if (exists) {
            throw new CustomException(ErrorCode.SAME_NAME_AND_ADDRESS);
        }
        // es.3 한개의 아이디에 3개의 가게를 생성할수없다
        long count = foodStoreRepository.countByUser(user);
        if (count >= 3) {
            throw new CustomException(ErrorCode.MAXIMUM_STORE);
        }

        FoodStore foodStore = new FoodStore(
                user,
                dto.getTitle(),
                dto.getAddress(),
                dto.getMinPrice(),
                dto.getOpenAt(),
                dto.getCloseAt()
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
                foodStore.getUpdatedAt()
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
                    foodStore.getUpdatedAt()
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
                categories
        );
    }

    // 가게 정보수정
    @Transactional
    public FoodStoreUpdateResponseDto update(Long foodStoreId, FoodStoreRequestDto dto) {
        FoodStore foodStore = foodStoreRepository.findById(foodStoreId).orElseThrow(
                () -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND)
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
    public void delete(Long foodStoreId, String token, String password) {
        String jwt = jwtUtil.substringToken(token);
        Long userId = Long.valueOf(jwtUtil.extractClaims(jwt).getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NOT_EXIST));

        // es.1 비밀번호가 일치하지 않을시
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_WRONG);
        }

        FoodStore foodStore = foodStoreRepository.findById(foodStoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOODSTORE_NOT_FOUND));

        foodStore.setDeletedAt(LocalDateTime.now());
        foodStoreRepository.save(foodStore);
    }
}
