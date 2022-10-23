package org.example.api.dto.graphql.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

}
