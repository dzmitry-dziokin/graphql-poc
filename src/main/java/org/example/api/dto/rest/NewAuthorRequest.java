package org.example.api.dto.rest;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewAuthorRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

}
