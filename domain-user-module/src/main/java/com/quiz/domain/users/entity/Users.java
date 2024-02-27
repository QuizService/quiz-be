package com.quiz.domain.users.entity;

import com.quiz.domain.users.enums.AuthType;
import com.quiz.global.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usersId;
    private String email;
    private String name;
    private AuthType authType;

    @Builder
    public Users(String email, String name, String provider) {
        this.email = email;
        this.name = name;
        this.authType = AuthType.findByProvider(provider);
    }
}
