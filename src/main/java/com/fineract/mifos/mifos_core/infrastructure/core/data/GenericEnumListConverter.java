package com.fineract.mifos.mifos_core.infrastructure.core.data;

import jakarta.persistence.AttributeConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GenericEnumListConverter <E extends Enum<E>> implements AttributeConverter<List<E>, String> {

    private static final String SPLIT_CHAR = ",";

    private final Class<E> clazz;

    public boolean isUnique() {
        return false;
    }

    protected GenericEnumListConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String convertToDatabaseColumn(List<E> values) {
        if (values.isEmpty()) {
            return null;
        }
        Stream<E> valueStream;
        if (isUnique()) {
            valueStream = values.stream().distinct();
        } else {
            valueStream = values.stream();
        }
        return valueStream.map(Enum::name).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<E> convertToEntityAttribute(String string) {
        if (StringUtils.isBlank(string)) {
            return List.of();
        }
        Stream<E> stream = Stream.of(string.split(SPLIT_CHAR)).map(e -> Enum.valueOf(clazz, e));
        if (isUnique()) {
            return stream.distinct().toList();
        }
        return stream.toList();
    }

}

