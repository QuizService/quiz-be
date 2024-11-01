package com.quiz.api.users;

import com.quiz.domain.users.dto.UserInfoDto;
import com.quiz.domain.users.service.UsersService;
import com.quiz.dto.ResponseDto;
import com.quiz.global.security.userdetails.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UsersController {
    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<ResponseDto<?>> getUserInfo(@AuthenticationPrincipal UserAccount user) {
        UserInfoDto response;
        if (user.getUsername() == null) {
            response = UserInfoDto.builder().build();
        } else {
            response = usersService.getUserInfo(user.getUsername());
        }
        return ResponseEntity.ok(ResponseDto.success(response));
    }

}
