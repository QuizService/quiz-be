package com.quiz.domain.users.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersRequestDto {
    private String email;
    private String name;
    private String provider;

    @Builder
    public UsersRequestDto(String email, String name, String provider) {
        this.email = email;
        this.name = name;
        this.provider = provider;
    }
}
