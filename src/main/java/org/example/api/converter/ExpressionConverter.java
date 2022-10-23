package org.example.api.converter;

import org.example.api.dto.graphql.request.Filter;
import org.example.repository.support.dto.SqlConditionExpression;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ExpressionConverter
    extends AbstractConverter<SqlConditionExpression, Filter.Expression, Filter.Expression> {

    public ExpressionConverter(ModelMapper mapper) {
        super(mapper, SqlConditionExpression.class, Filter.Expression.class, Filter.Expression.class);
    }
}
