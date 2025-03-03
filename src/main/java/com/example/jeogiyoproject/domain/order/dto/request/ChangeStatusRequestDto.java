package com.example.jeogiyoproject.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangeStatusRequestDto {

    @NotNull(message = "상태는 필수값입니다.")
    private String status;
}
