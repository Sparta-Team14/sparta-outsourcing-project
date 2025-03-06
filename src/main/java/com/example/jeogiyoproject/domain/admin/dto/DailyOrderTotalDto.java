package com.example.jeogiyoproject.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DailyOrderTotalDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime date;
    private long orderTotal;

    public DailyOrderTotalDto(int year, int month, int day, long orderCount) {
        this.date = LocalDateTime.of(year, month, day, 0, 0, 0, 0); // 입력받은 year, month, day로 날짜를 생성
        this.orderTotal = orderCount;
    }
}
