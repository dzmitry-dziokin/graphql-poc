package org.example.config.dgs;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DgsScalar(name = "LocalDate")
public class LocalDateScalar implements Coercing<LocalDate, String> {

    @Override
    public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDate) {
            return ((LocalDate) dataFetcherResult).format(DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            throw new CoercingSerializeException("Not a valid DateTime");
        }
    }

    @Override
    public @NotNull LocalDate parseValue(@NotNull Object input) throws CoercingParseValueException {
        return LocalDate.parse(input.toString(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public @NotNull LocalDate parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return LocalDate.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
        }

        throw new CoercingParseLiteralException("Value is not a valid ISO date time");
    }

}
