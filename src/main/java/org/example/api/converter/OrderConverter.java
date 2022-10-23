package org.example.api.converter;

import org.example.api.dto.graphql.common.sort.Order;
import org.example.api.dto.graphql.common.sort.SortOrderDirection;
import org.example.api.dto.graphql.request.InputOrder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Convertable<Sort.Order, InputOrder, Order> {

    @Override
    public Sort.Order toEntity(InputOrder dto) {

        return new Sort.Order(Sort.Direction.valueOf(dto.getOrder().name()), dto.getField());
    }

    @Override
    public Order toDto(Sort.Order entity) {
        return new Order(entity.getProperty(), SortOrderDirection.valueOf(entity.getDirection().name()));
    }
}
