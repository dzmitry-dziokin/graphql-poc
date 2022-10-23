package org.example.api.dto.graphql.request;

import lombok.Data;

import java.util.List;

@Data
public class InputSort {

    private List<InputOrder> orders;
}
