package com.quiz.api.questions;

import com.quiz.domain.questions.dto.QuestionIntegratedDto;
import com.quiz.domain.questions.dto.QuestionsResponseAdminDto;
import com.quiz.domain.questions.dto.QuestionsResponseDto;
import com.quiz.domain.questions.dto.QuestionsUpdateDto;
import com.quiz.domain.questions.service.QuestionFacade;
import com.quiz.dto.ResponseDto;
import com.quiz.global.security.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
@RestController
public class QuestionsController {
    private final QuestionFacade questionFacade;

    @Operation(summary = "questions 생성")
    @PostMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> saveQuestions(@PathVariable("quiz-id") Long quizId,
                                                        @RequestBody QuestionIntegratedDto request,
                                                        @LoginUser String email) {
        questionFacade.saveQuestions(request.getQuestionRequestDtos(), quizId, email);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @Operation(summary = "questions 수정")
    @PatchMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> updateQuestions(@PathVariable("quiz-id") Long quizId,
                                                          @RequestBody QuestionsUpdateDto request,
                                                          @LoginUser String email) {
        questionFacade.updateQuestions(request, quizId, email);

        return ResponseEntity.ok(ResponseDto.success());
    }

    @Operation(summary = "questions 조회 (생성자 전용)")
    @GetMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> getQuestionsByQuizId(@PathVariable("quiz-id") Long quizId,
                                                               @LoginUser String email) {
        List<QuestionsResponseAdminDto> questions = questionFacade.findAllByQuizId(quizId, email);
        return ResponseEntity.ok(ResponseDto.success(questions));
    }

    @Operation(summary = "questions 조회 (참여자 전용)")
    @GetMapping("/form/{endpoint}")
    public ResponseEntity<ResponseDto<?>> getQuestionsByEndpoint(@PathVariable("endpoint") @Parameter(description = "사용자에게 발급된 url의 뒷자리 8자") String endpoint) {
        List<QuestionsResponseDto> questions = questionFacade.findPageByEndpoint(endpoint);

        return ResponseEntity.ok(ResponseDto.success(questions));
    }


}
