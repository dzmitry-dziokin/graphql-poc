package org.example.api.converter;

import org.example.api.dto.graphql.request.Filter;
import org.example.repository.support.dto.SqlFilter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FilterConverter extends AbstractConverter<SqlFilter, Filter, Filter> {

    public FilterConverter(ModelMapper mapper) {
        super(mapper, SqlFilter.class, Filter.class, Filter.class);
    }

}
