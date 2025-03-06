package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.example.jeogiyoproject.global.exception.ErrorCode;
import com.example.jeogiyoproject.global.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FoodStoreServiceTest {

    @Mock
    private FoodStoreRepository foodStoreRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private FoodStoreService foodStoreService;

    @Test
    void 가게생성() {
        // given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "role", UserRole.OWNER);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        FoodStoreRequestDto requestDto = new FoodStoreRequestDto(
                "식당1",
                "식당주소",
                15000,
                LocalTime.parse("09:00:00"),
                LocalTime.parse("20:00:00")
        );

        // when
        FoodStoreResponseDto responseDto = foodStoreService.create(requestDto,userId);
        // then
        assertThat(responseDto).isNotNull();
        assertEquals("식당1", responseDto.getTitle());
        assertEquals("식당주소", responseDto.getAddress());
        assertEquals(15000, responseDto.getMinPrice());
        assertEquals(LocalTime.parse("09:00:00"), responseDto.getOpenAt());
        assertEquals(LocalTime.parse("20:00:00"), responseDto.getCloseAt());
    }

    @Test
    void role이_OWNER가_아닐때_에러반환() {
        // given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "role", UserRole.USER);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        FoodStoreRequestDto requestDto = new FoodStoreRequestDto(
                "식당1",
                "식당주소",
                15000,
                LocalTime.parse("09:00:00"),
                LocalTime.parse("20:00:00")
        );

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            foodStoreService.create(requestDto, userId);
        });

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
    }

    @Test
    void 이름과_주소가_동일하면_생성불가() {
        // given
        Long userId = 1L;
        String title = "식당1";
        String address = "식당주소";
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "role", UserRole.OWNER);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        given(foodStoreRepository.existsByTitleAndAddress(title, address)).willReturn(true);

        FoodStoreRequestDto requestDto = new FoodStoreRequestDto(
                "식당1",
                "식당주소",
                15000,
                LocalTime.parse("09:00:00"),
                LocalTime.parse("20:00:00")
        );

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            foodStoreService.create(requestDto, userId);
        });

        assertEquals(ErrorCode.SAME_NAME_AND_ADDRESS, exception.getErrorCode());
    }

    @Test
    void 한개의_아이디에_3개의_가게_생성불가(){
        // given
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(user, "role", UserRole.OWNER);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(foodStoreRepository.countByUser(user)).willReturn(3L);

        FoodStoreRequestDto requestDto = new FoodStoreRequestDto(
                "식당1",
                "식당주소",
                15000,
                LocalTime.parse("09:00:00"),
                LocalTime.parse("20:00:00")
        );

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            foodStoreService.create(requestDto, userId);
        });

        assertEquals(ErrorCode.MAXIMUM_STORE, exception.getErrorCode());
    }

    @Test
    void 가게_이름검색() {
        // given
        String title = "식당1";
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        FoodStore foodStore = new FoodStore();
        ReflectionTestUtils.setField(foodStore, "id", 1L);
        ReflectionTestUtils.setField(foodStore, "user", user);
        ReflectionTestUtils.setField(foodStore, "title", "식당1");
        ReflectionTestUtils.setField(foodStore, "address", "식당주소");
        ReflectionTestUtils.setField(foodStore, "minPrice", 15000);
        ReflectionTestUtils.setField(foodStore, "openAt", LocalTime.parse("09:00:00"));
        ReflectionTestUtils.setField(foodStore, "closeAt", LocalTime.parse("20:00:00"));

        given(foodStoreRepository.findByTitleContaining(title)).willReturn(Collections.singletonList(foodStore));

        // when
        List<FoodStoreResponseDto> result = foodStoreService.findStoreByTitle(title);

        // then
        FoodStoreResponseDto dto = result.get(0);
        assertEquals(foodStore.getId(), dto.getId());
        assertEquals(foodStore.getUserId(), dto.getUserId());
        assertEquals(foodStore.getTitle(), dto.getTitle());
        assertEquals(foodStore.getAddress(), dto.getAddress());
        assertEquals(foodStore.getMinPrice(), dto.getMinPrice());
        assertEquals(foodStore.getOpenAt(), dto.getOpenAt());
        assertEquals(foodStore.getCloseAt(), dto.getCloseAt());
    }

    @Test
    void 가게이름_없을때() {
        // given
        String title = "식당";
        given(foodStoreRepository.findByTitleContaining(title)).willReturn(Collections.emptyList());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            foodStoreService.findStoreByTitle(title);
        });

        assertEquals(ErrorCode.FOODSTORE_NOT_FOUND, exception.getErrorCode());
    }
}