package com.example.jeogiyoproject.domain.foodstore.service;

import com.example.jeogiyoproject.domain.foodstore.dto.req.FoodStoreRequestDto;
import com.example.jeogiyoproject.domain.foodstore.dto.res.FoodStoreResponseDto;
import com.example.jeogiyoproject.domain.foodstore.repository.FoodStoreRepository;
import com.example.jeogiyoproject.domain.menu.repository.MenuRepository;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(1L, responseDto.getId());
        assertEquals("식당1", responseDto.getTitle());
        assertEquals("식당주소", responseDto.getAddress());
        assertEquals(15000, responseDto.getMinPrice());
        assertEquals(LocalTime.parse("09:00:00"), responseDto.getOpenAt());
        assertEquals(LocalTime.parse("20:00:00"), responseDto.getCloseAt());
    }
}