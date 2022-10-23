package org.example.api.converter;

import org.example.api.dto.graphql.response.AuthorResponse;
import org.example.api.dto.rest.NewAuthorRequest;
import org.example.domain.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AuthorConverter extends AbstractIdentifiableConverter<Author, NewAuthorRequest, AuthorResponse> {

    public AuthorConverter(ModelMapper mapper) {
        super(mapper, Author.class, NewAuthorRequest.class, AuthorResponse.class);
    }

    @PostConstruct
    private void adjustMapping() {
        getToEntityTypeMap()
            .addMappings(mapping -> mapping.skip(Author::setId));
    }
}
