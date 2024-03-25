package com.quiz.api.quiz;

import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.domain.quiz.service.QuizFacade;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.service.UsersService;
import com.quiz.dto.MultiResponseDto;
import com.quiz.dto.ResponseDto;
import com.quiz.global.security.userdetails.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
@RestController
public class QuizController {
    private final QuizFacade quizFacade;
    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<ResponseDto<?>> saveQuiz(@RequestBody QuizRequestDto quizRequest,
                                                   @AuthenticationPrincipal UserAccount user) {
        Users users = findUsers(user);
        Long quizId = quizFacade.saveQuiz(quizRequest, users.getId());

        return ResponseEntity.ok(ResponseDto.success(quizId));
    }

    @PatchMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> updateQuiz(@PathVariable("quiz-id") Long quizId,
                                        @RequestBody QuizRequestDto quizRequest,
                                        @AuthenticationPrincipal UserAccount user) {
        Users users = findUsers(user);
        quizId = quizFacade.updateQuiz(quizRequest, quizId, users.getId());

        return ResponseEntity.ok(ResponseDto.success(quizId));
    }


    @GetMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> findQuizById(@PathVariable("quiz-id") Long quizId) {
        QuizResponseDto response = quizFacade.findById(quizId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<?>> findQuizByUser(@RequestParam("page") int page,
                                                         @RequestParam("size") int size,
                                                         @AuthenticationPrincipal UserAccount user) {
        Users users = findUsers(user);
        Page<QuizResponseDto> responsePage = quizFacade.findAllByUserId(users.getId(), page-1, size);
        List<QuizResponseDto> responses = responsePage.getContent();

        return ResponseEntity.ok(ResponseDto.success(new MultiResponseDto<>(responses, responsePage)));
    }

    @GetMapping("/endpoint/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> findEndpointByQuizId(@PathVariable("quiz-id") Long quizId) {
        String endpoint = quizFacade.findEndpointByQuizId(quizId);

        return ResponseEntity.ok(ResponseDto.success(endpoint));
    }

    @GetMapping("/form/{endpoint}")
    public ResponseEntity<ResponseDto<?>> findQuizByEndpoint(@PathVariable("endpoint") String endpoint) {

        QuizResponseDto response = quizFacade.findByEndPoint(endpoint);

        return ResponseEntity.ok(ResponseDto.success(response));
    }

    private Users findUsers(UserDetails user) {
        String email = user.getUsername();
        Users users = usersService.findByEmail(email);
        return users;
    }
}
