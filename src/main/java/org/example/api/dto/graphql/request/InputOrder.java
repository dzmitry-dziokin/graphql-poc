package org.example.api.dto.graphql.request;

import lombok.Data;
import org.example.api.dto.graphql.common.sort.SortOrderDirection;

@Data
public class InputOrder {

    private String field;
    private SortOrderDirection order;
}
