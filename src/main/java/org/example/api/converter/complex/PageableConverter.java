package org.example.api.converter.complex;

import org.example.api.dto.graphql.request.InputPage;
import org.example.api.dto.graphql.request.InputSort;
import org.springframework.data.domain.Pageable;

public class PageableConverter {

    public static Pageable toPageable(InputPage page, InputSort sort) {
        return null;
    }

}
