package com.example.jeogiyoproject.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private String status;
    private int code;
    private String message;

    public static ResponseEntity<ErrorResponse> errorResponseEntity(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(e.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().getReasonPhrase())
                        .code(errorCode.getStatus().value())
                        .message(e.getMessage())
                        .build());
    }
}
