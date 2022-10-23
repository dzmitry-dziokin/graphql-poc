package org.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Author;
import org.example.repository.support.NamedPageableQueryBuilder;
import org.example.repository.support.NamedQuery;
import org.example.repository.support.dto.SqlFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SpringJdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private Map<String, String> fieldToColumnMapping;   // could be dynamically loaded

    @PostConstruct
    void setup() {
        fieldToColumnMapping = Map.of(
            "id", "id",
            "firstName", "first_name",
            "lastName", "last_name",
            "dateOfBirth", "date_of_birth"
        );
    }

    @Override
    public long insert(Author author) {
        final String insertQuery = ""
            + "INSERT INTO authors (first_name, last_name, date_of_birth) VALUES "
            + "(:firstName, :lastName, :birthDate)";

        Map<String, Object> params = Map.of(
            "firstName", author.getFirstName(),
            "lastName", author.getLastName(),
            "birthDate", author.getDateOfBirth()
        );
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(insertQuery, parameterSource, keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    @Override
    public Page<Author> findAllFiltered(SqlFilter filter, Pageable pageable) {

        NamedQuery namedQuery = NamedPageableQueryBuilder.builder(fieldToColumnMapping)
            .select()
            .columns(Arrays.asList("*", "count(*) OVER() AS total_count"))
            .from("authors")
            .where(filter)
            .orderBy(pageable.getSort())
            .limited(pageable)
            .build();

        log.debug("Generated the following named query:\n{}", namedQuery);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(namedQuery.getQuery(), namedQuery.getParams());
        return convertToAuthorPage(rows, pageable);
    }

    private Page<Author> convertToAuthorPage(List<Map<String, Object>> rows, Pageable pageable) {

        List<Author> authors = rows.stream()
            .map(this::authorFromRow)
            .collect(Collectors.toList());

        return authors.isEmpty() ?
            new PageImpl<>(authors) : new PageImpl<>(authors, pageable, (Long) rows.get(0).get("total_count"));
    }

    private Author authorFromRow(Map<String, Object> row) {
        return new Author(
            ((Number) row.get("id")).longValue(),
            (String) row.get("first_name"),
            (String) row.get("last_name"),
            ((Timestamp) row.get("date_of_birth")).toLocalDateTime().toLocalDate()
        );
    }
}
