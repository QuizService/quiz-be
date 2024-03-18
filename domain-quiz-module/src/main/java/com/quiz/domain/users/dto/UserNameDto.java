package com.quiz.domain.users.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNameDto {
    private Long id;
    private String name;

    public UserNameDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
