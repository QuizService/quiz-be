package com.quiz.global.converter;

import com.quiz.domain.users.enums.Role;
import jakarta.persistence.AttributeConverter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.getValue();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(Role.class)
                .stream()
                .filter(e -> e.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("role not found"));
    }
}
