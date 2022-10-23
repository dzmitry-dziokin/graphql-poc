package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Identifiable<Long>, Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
