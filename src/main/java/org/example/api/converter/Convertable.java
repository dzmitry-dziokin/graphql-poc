package org.example.api.converter;

public interface Convertable<E, IN, OUT> {

    E toEntity(IN dto);

    OUT toDto(E entity);

}
