package com.quiz.api.questions;

import com.quiz.domain.questions.dto.QuestionIntegratedDto;
import com.quiz.domain.questions.dto.QuestionsResponseDto;
import com.quiz.domain.questions.service.QuestionFacade;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
@RestController
public class QuestionsController {
    private final QuestionFacade questionFacade;
    private final UsersService usersService;

    @PostMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> saveQuestions(@PathVariable("quiz-id") Long quizId,
                                                        @RequestBody QuestionIntegratedDto request,
                                                        @AuthenticationPrincipal UserAccount user) {
        Users users = usersService.findByEmail(user.getUsername());

        questionFacade.saveQuestions(request.getQuestionRequestDtos(), quizId, users.getId());

        return ResponseEntity.ok(ResponseDto.success());
    }

    @PatchMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> updateQuestions(@PathVariable("quiz-id") Long quizId,
                                                          @RequestBody QuestionIntegratedDto request,
                                                          @AuthenticationPrincipal UserAccount user) {
        Users users = usersService.findByEmail(user.getUsername());
        questionFacade.updateQuestions(request.getQuestionRequestDtos(), quizId, users.getId());

        return ResponseEntity.ok(ResponseDto.success());
    }

    @GetMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> getQuestionsByQuizId(@PathVariable("quiz-id") Long quizId,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("size") int size,
                                                               @AuthenticationPrincipal UserAccount user) {
        Users users = usersService.findByEmail(user.getUsername());
        Page<QuestionsResponseDto> questions = questionFacade.findPageByQuizId(quizId, users.getId(), page-1, size);
        List<QuestionsResponseDto> questionsList = questions.getContent();

        MultiResponseDto<QuestionsResponseDto> response = new MultiResponseDto<>(questionsList, questions);

        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/form/{endpoint}")
    public ResponseEntity<ResponseDto<?>> getQuestionsByEndpoint(@PathVariable("endpoint") String endpoint,
                                                                 @RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        Page<QuestionsResponseDto> questions = questionFacade.findPageByEndpoint(endpoint, page, size);
        List<QuestionsResponseDto> questionsList = questions.getContent();

        MultiResponseDto<QuestionsResponseDto> response = new MultiResponseDto<>(questionsList, questions);

        return ResponseEntity.ok(ResponseDto.success(response));
    }

}
