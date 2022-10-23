package org.example.api.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.language.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.converter.Convertable;
import org.example.api.dto.graphql.request.Filter;
import org.example.api.dto.graphql.request.InputPage;
import org.example.api.dto.graphql.request.InputSort;
import org.example.api.dto.graphql.response.AuthorResponse;
import org.example.api.dto.graphql.response.PageableAuthorResponse;
import org.example.api.dto.rest.NewAuthorRequest;
import org.example.domain.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.support.dto.SqlFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class AuthorQuery {

    private final Convertable<Author, NewAuthorRequest, AuthorResponse> authorConverter;
    private final Convertable<Sort, InputSort, org.example.api.dto.graphql.common.sort.Sort> sortConverter;
    private final Convertable<SqlFilter, Filter, Filter> filterConverter;
    private final AuthorRepository authorRepository;

    @DgsQuery
    public PageableAuthorResponse<AuthorResponse> authors(
        @InputArgument InputPage page,
        @InputArgument Filter filter,
        @InputArgument InputSort sort,
        DgsDataFetchingEnvironment dgsEnvironment
    ) {

        log.info("Requesting authors data (requested fields={}): page={}, filter={}, sort=: {}",
            extractRequestedFields(dgsEnvironment), page, filter, sort);
        return getAuthors(
            PageRequest.of(page.getPageNumber(), page.getPageSize(), sortConverter.toEntity(sort)),
            filterConverter.toEntity(filter)
        );
    }

    private PageableAuthorResponse<AuthorResponse> getAuthors(PageRequest page, SqlFilter sqlFilter) {
        Page<Author> authors = authorRepository.findAllFiltered(sqlFilter, page);
        List<AuthorResponse> items = authors.stream()
            .map(authorConverter::toDto)
            .collect(Collectors.toList());

        return new PageableAuthorResponse<>(
            items,
            sortConverter.toDto(page.getSort()),
            authors.getNumber(),
            authors.getSize(),
            authors.getTotalElements()
        );

    }

    private List<String> extractRequestedFields(DgsDataFetchingEnvironment dgsEnvironment) {
        Field authorsField = dgsEnvironment.getMergedField().getFields().get(0);
        return authorsField.getSelectionSet().getSelections().stream()
            .filter(selection -> ((Field) selection).getName().equals("items"))
            .findFirst()
            .map(selection -> ((Field) selection).getSelectionSet().getSelections().stream()
                .map(selection1 -> ((Field) selection1).getName())
                .collect(Collectors.toList())
            )
            .orElse(Collections.emptyList());
    }
}
