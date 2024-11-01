package com.quiz.api.auth;


import com.quiz.global.security.dto.IdToken;
import com.quiz.global.security.dto.TokenDto;
import com.quiz.global.security.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
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
