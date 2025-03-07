package com.example.jeogiyoproject.domain.order.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginationResponse<T> {
    private final List<T> content;

    private final int pageNumber;

    private final int pageSize;

    private final long totalElements;

    private final int totalPages;

    private final boolean isLast;

    private final boolean isFirst;

    private final boolean isSorted;

    public PaginationResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.isLast = page.isLast();
        this.isFirst = page.isFirst();
        this.isSorted = page.getSort().isSorted();
    }
}