package com.quiz.global.security.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.quiz.domain.users.dto.UsersRequestDto;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.service.UsersService;
import com.quiz.global.security.dto.TokenDto;
import com.quiz.global.security.exception.AuthException;
import com.quiz.global.security.exception.code.AuthErrorCode;
import com.quiz.global.security.jwt.JwtTokenizer;
import com.quiz.global.security.userdetails.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Transactional
@Service
public class AuthService {
    private final GoogleIdTokenVerifier verifier;
    private final JwtTokenizer jwtTokenizer;
    private final UsersService usersService;

    public AuthService(@Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId, JwtTokenizer jwtTokenizer, UsersService usersService) {
        this.jwtTokenizer = jwtTokenizer;
        this.usersService = usersService;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        this.verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singleton(clientId))
                .build();
    }

    public TokenDto login(String code) {
        try {
            GoogleIdToken idToken = verifier.verify(code);

            if (idToken == null) {
                log.info("idToken is null");
                return null;
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            UsersRequestDto dto = UsersRequestDto.builder()
                    .email(email)
                    .name(firstName + lastName)
                    .provider("google")
                    .build();
            Users users = usersService.findOrCreateUsers(dto);

            String accessToken = "Bearer " + jwtTokenizer.createAccessToken(email);
            String refreshToken = "Bearer " + jwtTokenizer.createRefreshTokenWhenLogin(users.getId());
//            log.info("access token = {}", accessToken);
            saveAuthentication(users);

            return TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            log.error("error : ", e);
            throw new AuthException(AuthErrorCode.LOGIN_FAILED);
        }
    }

    public void saveAuthentication(Users users) {
        UserDetails userDetails = new UserAccount(users);

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(users.getRole()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
