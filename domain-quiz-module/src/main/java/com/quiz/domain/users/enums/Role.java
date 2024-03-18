package com.quiz.domain.users.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN", "admin"),
    USER("USER", "user");

    private final String code;
    private final String value;

    Role(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
