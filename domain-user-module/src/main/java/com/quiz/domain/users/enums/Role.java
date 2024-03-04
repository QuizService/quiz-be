package com.quiz.domain.users.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ADMIN", "admin"),
    USER("USER", "user");

    private final String role;
    private final String title;

    Role(String role, String title) {
        this.role = role;
        this.title = title;
    }
}
