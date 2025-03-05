package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.user.controller.UserController;
import com.example.jeogiyoproject.domain.user.dto.request.UserAddressUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserDeleteRequestDto;
import com.example.jeogiyoproject.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserAddressUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserPasswordUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.AuthUserArgumentResolver;
import com.example.jeogiyoproject.global.config.PasswordEncoder;
import com.example.jeogiyoproject.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void userid로_조회할_수_있다() {
        // given
        String email = "test@gmail.com";
        long id = 1L;
        String password = "1234";
        String name = "test";
        String address = "seoul";
        User user = new User(email, password, name, address, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", id);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponseDto userResponseDto = userService.findUser(id);

        // then
        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseDto.getId()).isEqualTo(id);
        assertThat(userResponseDto.getName()).isEqualTo(name);
        assertThat(userResponseDto.getAdress()).isEqualTo(address);
        assertThat(userResponseDto.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void userid_가_없으면_예외처리한다() {
        long id = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.findUser(id), "유저가 없습니다.");
    }

    @Test
    void address_를_변경해보자() {
        String email = "test@gmail.com";
        long id = 1L;
        String password = "1234";
        String name = "test";
        String address = "seoul";
        User user = new User(email, password, name, address, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", id);

        long id2 = 1L;
        String address2 = "busan";
        String password2= "12345";

        UserAddressUpdateRequestDto userAddressUpdateRequestDto = new UserAddressUpdateRequestDto();
        ReflectionTestUtils.setField(userAddressUpdateRequestDto, "address", address2);
        ReflectionTestUtils.setField(userAddressUpdateRequestDto, "password", password2);

        // when
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(),anyString())).willReturn(true);

        UserAddressUpdateResponseDto update = userService.update(55L, userAddressUpdateRequestDto);

        assertThat(update.getId()).isEqualTo(id);
        assertThat(update.getAddress()).isEqualTo(address2);

    }

    @Test
    void 회원_삭제할_수_있음() {
        // Given
        long id = 1L;
        String password = "1234";

        UserDeleteRequestDto requestDto = new UserDeleteRequestDto();
        ReflectionTestUtils.setField(requestDto, "password", password);

        User user = new User("test@gmail.com", password, "test", "seoul", UserRole.USER);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // When
        userService.deleteUser(id, requestDto);

        // Then
        verify(userRepository, times(1)).deleteById(id);

    }

    @Test
    void change_password() {
        // given
        long id = 1L;
        String password = "1234";
        String newPassword = "5678";
        UserPasswordUpdateRequestDto requestDto = new UserPasswordUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "password", password);
        ReflectionTestUtils.setField(requestDto, "newPassword", newPassword);
        User user = new User("test@gmail.com", password, "test", "seoul", UserRole.USER);
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        // when
        UserPasswordUpdateResponseDto responseDto = userService.updatePassword(id, requestDto);
        // then
        assertThat(responseDto.getEmail()).isEqualTo(user.getEmail());
    }
}
