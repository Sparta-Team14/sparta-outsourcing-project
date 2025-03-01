package com.example.jeogiyoproject.domain.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Status {
    WAIT(0), CANCELED(1), ACCEPTED(2), DELIVERED(3), COMPLETED(4);

    final int level;

    public static Status of(String status) {
        return Arrays.stream(Status.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상태입니다."));
    }
}
