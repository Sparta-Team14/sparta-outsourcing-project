package com.example.jeogiyoproject.domain.user.service;

import com.example.jeogiyoproject.domain.admin.service.AdminService;
import com.example.jeogiyoproject.domain.user.dto.request.RoleUpdateRequestDto;
import com.example.jeogiyoproject.domain.user.dto.response.RoleUpdateResponseDto;
import com.example.jeogiyoproject.domain.user.entity.User;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.global.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService userAdminService;

    @Test
    void 유저_권한이_성공적으로_변경이_되는지_확인한다() {
        String email = "test@gmail.com";
        long id = 1L;
        String password = "1234";
        String name = "test";
        String address = "seoul";
        User user = new User(email, password, name, address, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", id);

        String userRole = "OWNER";
        RoleUpdateRequestDto requestDto = new RoleUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "role", userRole);

        BDDMockito.given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        RoleUpdateResponseDto roleUpdateResponseDto = userAdminService.updateRole(id, requestDto);

        assertThat(roleUpdateResponseDto.getId()).isEqualTo(id);
        assertThat(roleUpdateResponseDto.getEmail()).isEqualTo(email);
        assertThat(roleUpdateResponseDto.getUserRole()).isEqualTo(UserRole.OWNER);
    }

    @Test
    void 예외처리가_나오는지_확인() {
        String email = "test@gmail.com";
        long id = 1L;
        String password = "1234";
        String name = "test";
        String address = "seoul";
        User user = new User(email, password, name, address, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", id);

        String userRole = "OWNER";
        RoleUpdateRequestDto requestDto = new RoleUpdateRequestDto();
        ReflectionTestUtils.setField(requestDto, "role", userRole);

        BDDMockito.given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userAdminService.updateRole(id,requestDto), "유저가 없습니다.");
    }
}
