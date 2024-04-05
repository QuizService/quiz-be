package com.quiz.api.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/api/v1/test")
    public String test() {
        log.info("api test");
        return "success";
    }
}
