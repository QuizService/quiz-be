package com.quiz.api.quiz;

import com.quiz.domain.questions.dto.QuestionIntegratedDto;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.domain.quiz.service.QuizFacade;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.service.UsersService;
import com.quiz.global.security.userdetails.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
@RestController
public class QuizController {
    private final QuizFacade quizFacade;
    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<?> saveQuiz(@RequestBody QuizRequestDto quizRequest,
                                      @AuthenticationPrincipal UserAccount user) {
        Users users = findUsers(user);
        Long quizId = quizFacade.saveQuiz(quizRequest, users.getId());

        return ResponseEntity.ok(quizId);
    }

    @PatchMapping("/{quizId}")
    public ResponseEntity<?> updateQuiz(@PathVariable("quizId") Long quizId,
                                        @RequestBody QuizRequestDto quizRequest,
                                        @AuthenticationPrincipal UserAccount user) {
        Users users = findUsers(user);
        quizId = quizFacade.updateQuiz(quizRequest, quizId, users.getId());

        return ResponseEntity.ok(quizId);
    }

    @PatchMapping("/question/{quizId}")
    public ResponseEntity<?> saveQuestionAndReturnEndpoint(@PathVariable("quizId") Long quizId,
                                                           @RequestBody QuestionIntegratedDto questionIntegratedDto,
                                                           @AuthenticationPrincipal UserAccount user) {
        Users users = findUsers(user);
        String endPoint = quizFacade.saveQuestionsAndReturnQuizEndpoint(questionIntegratedDto, quizId, users.getId());

        return ResponseEntity.ok(endPoint);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> findQuizById(@PathVariable("quizId") Long quizId) {
        QuizResponseDto response = quizFacade.findById(quizId);

        return ResponseEntity.ok(response);
    }



    private Users findUsers(UserDetails user) {
        String email = user.getUsername();
        Users users = usersService.findByEmail(email);
        return users;
    }
}
