package org.example.repository.support;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;

@Getter
@ToString
public class NamedQuery {

    private final String query;
    private final Map<String, Object> params;

    NamedQuery(String query, Map<String, Object> params) {
        this.query = query;
        this.params = Collections.unmodifiableMap(params);
    }
}
