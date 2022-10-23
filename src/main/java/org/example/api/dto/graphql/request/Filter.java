package org.example.api.dto.graphql.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.repository.support.dto.FilterMatchType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private Expression expression;
    private Filter and;
    private Filter or;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expression {

        private String field;
        private Object value;
        private FilterMatchType match;
    }
}
