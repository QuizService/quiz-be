package com.quiz.api.auth;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.quiz.api.users.IdToken;
import com.quiz.api.users.TokenDto;
import com.quiz.global.security.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/googleLogin")
    public ResponseEntity<?> googleAuthLogin(@RequestBody IdToken request, HttpServletResponse response) {
        TokenDto tokenDto = authService.login(request.code());
        response.addHeader("Authorization", tokenDto.accessToken());
        response.addHeader("Refresh", tokenDto.refreshToken());

        return ResponseEntity.ok().build();
    }
}
