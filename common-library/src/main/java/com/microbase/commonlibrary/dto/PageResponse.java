package com.microbase.commonlibrary.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public static <S, T> PageResponse<T> from(Page<S> page, Function<S, T> mapper) {
        return from(page.map(mapper));
    }
}