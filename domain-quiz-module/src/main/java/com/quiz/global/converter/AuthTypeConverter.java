package com.quiz.global.converter;

import com.quiz.domain.users.enums.AuthType;
import jakarta.persistence.AttributeConverter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

public class AuthTypeConverter implements AttributeConverter<AuthType, String> {
    @Override
    public String convertToDatabaseColumn(AuthType attribute) {
        return attribute.getValue();
    }

    @Override
    public AuthType convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(AuthType.class)
                .stream()
                .filter(e -> e.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("auth type not found"));
    }
}
