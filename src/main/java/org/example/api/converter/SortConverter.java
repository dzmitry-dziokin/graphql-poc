package org.example.api.converter;

import lombok.RequiredArgsConstructor;
import org.example.api.dto.graphql.common.sort.Order;
import org.example.api.dto.graphql.request.InputOrder;
import org.example.api.dto.graphql.request.InputSort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SortConverter implements Convertable<Sort, InputSort, org.example.api.dto.graphql.common.sort.Sort> {

    private final Convertable<Sort.Order, InputOrder, Order> orderConverter;

    @Override
    public org.example.api.dto.graphql.common.sort.Sort toDto(Sort entity) {
        List<Order> orders = entity.get()
            .map(orderConverter::toDto)
            .collect(Collectors.toList());
        return new org.example.api.dto.graphql.common.sort.Sort(orders);
    }

    @Override
    public Sort toEntity(InputSort dto) {
        List<Sort.Order> orders = dto.getOrders().stream()
            .map(orderConverter::toEntity)
            .collect(Collectors.toList());
        return Sort.by(orders);
    }
}
