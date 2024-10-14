package com.neil.project.config.converter;

import jakarta.persistence.AttributeConverter;

/**
 * @author nihao
 * @date 2024/10/10
 */
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean boolValue) {
        return (boolValue != null && boolValue) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String stringValue) {
        return "Y".equals(stringValue);
    }
}
