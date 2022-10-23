package org.example.repository.support.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlFilter {

    private SqlConditionExpression expression;
    private SqlFilter and;
    private SqlFilter or;

}
