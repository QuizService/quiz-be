package com.quiz.dto.users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsersRequestDto {
    private String email;
    private String name;
    private String provider;
}
