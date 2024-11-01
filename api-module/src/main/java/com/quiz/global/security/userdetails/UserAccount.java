package com.quiz.global.security.userdetails;

import com.quiz.domain.users.entity.Users;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {
    private Users users;

    public UserAccount(final Users users) {
        super(users.getEmail(), "", List.of(new SimpleGrantedAuthority(users.getRole())));
        this.users = users;
    }


    public UserAccount(String email, String role) {
        super(email, "", List.of(new SimpleGrantedAuthority(role)));
    }
}
