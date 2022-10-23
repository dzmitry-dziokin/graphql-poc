package org.example.repository.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.repository.support.dto.FilterMatchType;
import org.example.repository.support.dto.SqlConditionExpression;
import org.example.repository.support.dto.SqlFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NamedPageableQueryBuilder {

    private static final Map<FilterMatchType, String> MATCH_TYPE_TO_OPERATOR = Map.of(
        FilterMatchType.EQ, "=",
        FilterMatchType.GT, ">",
        FilterMatchType.LT, "<"
    );

    private final Map<String, String> fieldToColumnMapping;
    private final Map<String, Object> namedParameters = new HashMap<>();
    private final Map<String, Integer> namedParametersCounter = new HashMap<>();

    private String query;

    public static NamedPageableQueryBuilder builder(Map<String, String> fieldToColumnMapping) {
        return new NamedPageableQueryBuilder(fieldToColumnMapping);
    }

    public Select select() {

        return new Select();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public abstract class BuildablePart {

        public final NamedQuery build() {
            return new NamedQuery(query, namedParameters);
        }
    }

    public class Select {

        private Select() {
            query = "SELECT";
        }

        public From columns(List<String> columns) {
            query += columns.stream()
                .collect(Collectors.joining(", ", "\n  ", ""));
            return new From();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class From {

        public Where from(String table) {
            query += String.format("\nFROM %s", table);
            return new Where();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Where extends BuildablePart {

        public Order where(SqlFilter filter) {
            query += String.format("\nWHERE\n  %s", processInternally(filter).orElse(""));
            return new Order();
        }

        public Order skip() {
            return new Order();
        }

        private Optional<String> processInternally(SqlFilter current) {
            if (!hasExpression(current)) {
                return Optional.empty();
            }

            String result = buildExpression(current.getExpression());

            result += processInternally(current.getAnd())
                .map(and -> " AND " + and)
                .orElse("");

            result += processInternally(current.getOr())
                .map(or -> " OR " + or)
                .orElse("");

            return Optional.of(result);

        }

        private String buildExpression(SqlConditionExpression expression) {
            String field = expression.getField();
            Object value = expression.getValue();
            String operator = MATCH_TYPE_TO_OPERATOR.get(expression.getMatch());

            String namedParameter = generateNamedParameterName(field);
            namedParameters.put(namedParameter, value);

            String columnName = fieldToColumnMapping.get(field);
            if (columnName == null) {
                log.warn("There's no column associated with the following key: {}", field);
                throw new IllegalStateException("No field to column mapping for key: " + field);
            }
            return String.format("%s %s :%s", columnName, operator, namedParameter);

        }

        private String generateNamedParameterName(String fieldName) {
            Integer fieldOccurrenceCount = namedParametersCounter.merge(fieldName, 1, Integer::sum);
            return String.format("fltr_%s_%d", fieldName, fieldOccurrenceCount);
        }

        private boolean hasExpression(SqlFilter filter) {
            if (filter == null) {
                return false;
            }
            SqlConditionExpression expression = filter.getExpression();
            return expression != null && StringUtils.isNotBlank(expression.getField()) && expression.getValue() != null;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Order extends BuildablePart {

        public Limit orderBy(Sort sort) {
            query += String.format("\nORDER BY%s",
                sort.stream()
                    .map(this::fromOrder)
                    .collect(Collectors.joining(",", "\n  ", ""))
            );
            return new Limit();
        }

        private String fromOrder(Sort.Order order) {
            String column = fieldToColumnMapping.get(order.getProperty());
            if (StringUtils.isBlank(column)) {
                log.warn("There's no column associated with the following key: {}", order.getProperty());
                throw new IllegalStateException("No field to column mapping for key: " + order.getProperty());
            }
            return String.format("%s %s", column, order.getDirection().name());
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Limit extends BuildablePart {

        public BuildablePart limited(Pageable page) {
            query += String.format("\nOFFSET %d FETCH FIRST %d ROWS ONLY",
                (page.getPageNumber() - 1) * page.getPageSize(), page.getPageSize());
            return this;
        }
    }

}
