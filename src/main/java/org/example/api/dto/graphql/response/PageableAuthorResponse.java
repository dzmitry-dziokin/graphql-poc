package org.example.api.dto.graphql.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.api.dto.graphql.common.sort.Sort;

import java.util.List;

@Data
@AllArgsConstructor
public class PageableAuthorResponse<T> {

    private List<T> items;
    private Sort sort;
    private int pageNumber;
    private int pageSize;
    private long total;
}
