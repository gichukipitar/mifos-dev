package com.fineract.mifos.mifos_core.infrastructure.core.service;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Data
public class PagedRequest<T> {

    public static final int DEFAULT_PAGE_SIZE = 50;

    private T request;

    private int page;
    private int size = DEFAULT_PAGE_SIZE;

    private final List<SortOrder> sorts = new ArrayList<>();

    public Optional<T> getRequest() {
        return Optional.ofNullable(request);
    }

    public PageRequest toPageable() {
        if (isEmpty(sorts)) {
            return PageRequest.of(page, size);
        } else {
            List<Sort.Order> orders = sorts.stream().map(SortOrder::toOrder).toList();
            return PageRequest.of(page, size, Sort.by(orders));
        }
    }

    @Data
    @SuppressWarnings({ "unused" })
    private static final class SortOrder {

        private Sort.Direction direction;
        private String property;

        private Sort.Order toOrder() {
            return new Sort.Order(direction, property);
        }
    }
}
