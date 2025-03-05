package com.example.jeogiyoproject.domain.user.controller;

import com.example.jeogiyoproject.domain.user.dto.response.UserResponseDto;
import com.example.jeogiyoproject.domain.user.enums.UserRole;
import com.example.jeogiyoproject.domain.user.repository.UserRepository;
import com.example.jeogiyoproject.domain.user.service.UserService;
import com.example.jeogiyoproject.global.AuthUserArgumentResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(AuthUserArgumentResolver.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void 회원_단건_조회() throws Exception {
        // given
        long id = 1L;
        String name = "test";
        String adress = "seoul";
        UserRole userRole = UserRole.USER;
        UserResponseDto userResponseDto = new UserResponseDto(id, name, adress, userRole);

        when(userService.findUser(id)).thenReturn(userResponseDto);

        // when & then
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.adress").value(adress))
                .andExpect(jsonPath("$.role").value(userRole.name()));
    }
}
