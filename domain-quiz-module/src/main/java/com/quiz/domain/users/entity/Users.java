package com.quiz.domain.users.entity;

import com.quiz.domain.users.enums.AuthType;
import com.quiz.domain.users.enums.Role;
import com.quiz.global.baseentity.BaseEntity;
import com.quiz.global.converter.AuthTypeConverter;
import com.quiz.global.converter.RoleConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users", indexes = {@Index(name = "idx_pk_name", columnList = "id, name"),
        @Index(name = "idx_email", columnList = "email")})
@Entity
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String picture;

    @Column(name = "auth_type")
    @Convert(converter = AuthTypeConverter.class)
    private AuthType authType;

    @Convert(converter = RoleConverter.class)
    private Role role;

    @Column(name = "refresh_token")
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
        return this.role.getCode();
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
