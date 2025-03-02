package com.example.jeogiyoproject.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    // Auth

    // Menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴번호 입니다."),

    // FoodStore
    FOODSTORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게번호 입니다."),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문번호 입니다."),
    ORDER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 주문 요청입니다."),
    NOT_FOODSTORE_OWNER(HttpStatus.FORBIDDEN, "해당 매장의 사장이 아닙니다."),
    CHANGE_STATUS_ERROR(HttpStatus.BAD_REQUEST, "상태를 변경할 수 없습니다."),
    NOT_ORDER_USER_ID(HttpStatus.FORBIDDEN, "주문한 회원번호가 아닙니다."),

    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
