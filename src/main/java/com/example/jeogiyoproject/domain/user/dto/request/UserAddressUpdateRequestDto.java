package com.example.jeogiyoproject.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserAddressUpdateRequestDto {
    private String password;
    private String address;
}
