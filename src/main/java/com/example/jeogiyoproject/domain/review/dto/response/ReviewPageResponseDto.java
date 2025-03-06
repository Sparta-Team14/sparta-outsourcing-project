package com.example.jeogiyoproject.domain.review.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ReviewPageResponseDto {
    private final List<ReviewResponseDto> content;

    private final int size;

    private final int number;

    private final long totalElements;

    private final int totalPages;


    public ReviewPageResponseDto(Page<ReviewResponseDto> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.number = page.getNumber() + 1;
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
