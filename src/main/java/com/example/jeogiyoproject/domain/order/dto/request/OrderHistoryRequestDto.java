package com.example.jeogiyoproject.domain.order.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class OrderHistoryRequestDto {

    private String foodstoreTitle;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startAt = LocalDate.now().minusMonths(1);

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "조회 종료일 값은 오늘 날짜 이전이어야 합니다.")
    private LocalDate endAt = LocalDate.now();
}
