package com.example.jeogiyoproject.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MonthlyOrderTotalDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private LocalDateTime date;
    private Long orderTotal;

    public MonthlyOrderTotalDto(int year, int month, Long orderTotal) {
        this.date = LocalDateTime.of(year, month, 1, 0, 0, 0, 0); // year, month, 1일로 LocalDateTime 생성
        this.orderTotal = orderTotal;
    }
}
