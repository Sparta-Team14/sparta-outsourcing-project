package com.example.jeogiyoproject.domain.review.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class ReviewPageResponseDto {
    private final List<ReviewResponseDto> content;

    private final int size;

    private final int number;

    private final long totalElements;

    private final int totalPages;

    // 여기 모든 정보중에 원하는 정보만 가져오겟다 이거지; dto는 내가정한 필드내용만 갖고잇으니까.
    public ReviewPageResponseDto(Page<ReviewResponseDto> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.number = page.getNumber() + 1;
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
