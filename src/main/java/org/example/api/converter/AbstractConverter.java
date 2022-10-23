package org.example.api.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

import java.util.Objects;

public class AbstractConverter<E, IN, OUT> implements Convertable<E, IN, OUT> {

    private final ModelMapper mapper;

    protected final Class<E> entityClass;
    protected final Class<IN> dtoInClass;
    protected final Class<OUT> dtoOutClass;

    AbstractConverter(
        ModelMapper mapper,
        Class<E> entityClass,
        Class<IN> dtoInClass,
        Class<OUT> dtoOutClass
    ) {
        this.mapper = mapper;
        this.entityClass = entityClass;
        this.dtoInClass = dtoInClass;
        this.dtoOutClass = dtoOutClass;
        mapper.createTypeMap(entityClass, dtoOutClass);
        mapper.createTypeMap(dtoInClass, entityClass);
    }

    @Override
    public E toEntity(IN dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, entityClass);
    }

    @Override
    public OUT toDto(E entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, dtoOutClass);
    }

    protected ModelMapper getMapper() {
        return mapper;
    }

    TypeMap<IN, E> getToEntityTypeMap() {
        return mapper.getTypeMap(dtoInClass, entityClass);
    }

    TypeMap<E, OUT> getToDtoTypeMap() {
        return mapper.getTypeMap(entityClass, dtoOutClass);
    }

}
