package org.example.api.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.api.converter.Convertable;
import org.example.api.dto.graphql.response.AuthorResponse;
import org.example.api.dto.rest.NewAuthorRequest;
import org.example.domain.Author;
import org.example.repository.AuthorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorRestController {

    private final Convertable<Author, NewAuthorRequest, AuthorResponse> authorConverter;
    private final AuthorRepository authorRepository;

    @PostMapping
    public ResponseEntity<Long> addNewAuthor(@RequestBody NewAuthorRequest newAuthor) {
        Author author = authorConverter.toEntity(newAuthor);
        log.info("Adding a new author: {}", author);
        return ResponseEntity.ok(authorRepository.insert(author));
    }
}
