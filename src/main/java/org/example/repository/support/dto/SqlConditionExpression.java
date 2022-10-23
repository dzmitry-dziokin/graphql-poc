package org.example.repository.support.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlConditionExpression {

    private String field;
    private Object value;
    private FilterMatchType match;
}
