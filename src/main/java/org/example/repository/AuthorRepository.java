package org.example.repository;

import org.example.domain.Author;
import org.example.repository.support.dto.SqlFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorRepository {

    long insert(Author author);
    Page<Author> findAllFiltered(SqlFilter filter, Pageable pageable);
}
