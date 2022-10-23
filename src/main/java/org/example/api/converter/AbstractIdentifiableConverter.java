package org.example.api.converter;

import org.example.domain.Identifiable;
import org.modelmapper.ModelMapper;

public class AbstractIdentifiableConverter<E extends Identifiable<Long>, IN, OUT>
    extends AbstractConverter<E, IN, OUT> {

    AbstractIdentifiableConverter(
        ModelMapper mapper,
        Class<E> entityClass,
        Class<IN> dtoInClass,
        Class<OUT> dtoOutClass
    ) {
        super(mapper, entityClass, dtoInClass, dtoOutClass);
        getToEntityTypeMap().addMappings(mapping -> mapping.skip(E::setId));

    }
}
