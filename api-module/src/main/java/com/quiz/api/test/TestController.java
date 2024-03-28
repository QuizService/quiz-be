package com.quiz.api.test;

import com.quiz.global.security.userdetails.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/api/v1/hello")
    public String test(@AuthenticationPrincipal UserAccount user) {
        log.info("user email : {}", user.getUsername());
        return "hello";
    }
}
