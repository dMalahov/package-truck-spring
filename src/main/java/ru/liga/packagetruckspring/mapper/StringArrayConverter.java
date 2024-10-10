package ru.liga.packagetruckspring.mapper;

import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Component
@Converter
public class StringArrayConverter implements AttributeConverter<String[], String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        if (attribute == null) {
            return null;
        }
        return String.join(SPLIT_CHAR, attribute);
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData.split(SPLIT_CHAR);
    }
}
