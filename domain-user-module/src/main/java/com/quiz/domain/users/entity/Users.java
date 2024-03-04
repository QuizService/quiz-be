package com.quiz.domain.users.entity;

import com.quiz.domain.users.enums.AuthType;
import com.quiz.domain.users.enums.Role;
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
    private String picture;
    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder
    public Users(String email, String name, String picture, String provider, Role role, String refreshToken) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.authType = AuthType.findByProvider(provider);
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public String getRole() {
        return this.role.getRole();
    }

    public void update(String name, String picture) {
        if(name != null) {
            this.name = name;
        }
        if(picture != null) {
            this.picture = picture;
        }
    }

    public void setRefreshToken(String refreshToken) {
        if(refreshToken != null) {
            this.refreshToken = refreshToken;
        }
    }

}
