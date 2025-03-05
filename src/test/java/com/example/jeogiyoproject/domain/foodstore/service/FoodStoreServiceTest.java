package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.entity.FoodStore;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
        String token = "token";

        FoodStoreRequestDto requestDto = new FoodStoreRequestDto(
                "식당1",
                "식당주소",
                15000,
                LocalTime.parse("09:00:00"),
                LocalTime.parse("20:00:00")
        );

        // when
        FoodStoreResponseDto responseDto = foodStoreService.create(requestDto,token);
        // then
        assertEquals(1L, responseDto.getId());
        assertEquals("식당1", responseDto.getTitle());
        assertEquals("식당주소", responseDto.getAddress());
        assertEquals(15000, responseDto.getMinPrice());
        assertEquals(LocalTime.parse("09:00:00"), responseDto.getOpenAt());
        assertEquals(LocalTime.parse("20:00:00"), responseDto.getCloseAt());
    }
}