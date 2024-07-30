package com.quiz.api.quiz;

import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.domain.quiz.service.QuizFacade;
import com.quiz.domain.users.service.UsersService;
import com.quiz.dto.MultiResponseDto;
import com.quiz.dto.ResponseDto;
import com.quiz.global.security.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
@RestController
public class QuizController {
    private final QuizFacade quizFacade;
    private final UsersService usersService;

    @Operation(summary = "quiz 생성 (questions 제외)")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> saveQuiz(@RequestBody @Valid QuizRequestDto quizRequest,
                                                   @LoginUser String email) {
        Long quizId = quizFacade.saveQuiz(quizRequest, email);

        return ResponseEntity.ok()
                .body(ResponseDto.success(quizId));
    }

    @Operation(summary = "quiz 업데이트 (questions 제외)")
    @PatchMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> updateQuiz(@PathVariable("quiz-id") Long quizId,
                                                     @RequestBody QuizRequestDto quizRequest,
                                                     @LoginUser String email) {
        quizId = quizFacade.updateQuiz(quizRequest, quizId, email);

        return ResponseEntity.ok()
                .body(ResponseDto.success(quizId));
    }

    @Operation(summary = "quiz 조회 (questions 제외), 생성자 전용")
    @GetMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> findQuizById(@PathVariable("quiz-id") Long quizId) {
        QuizResponseDto response = quizFacade.findById(quizId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @Operation(summary = "user가 만든 전체 quiz 조회")
    @GetMapping
    public ResponseEntity<ResponseDto<?>> findQuizByUser(@RequestParam("page") int page,
                                                         @RequestParam("size") int size,
                                                         @LoginUser String email) {
        Page<QuizResponseDto> responsePage = quizFacade.findAllByUserId(email, page - 1, size);
        List<QuizResponseDto> responses = responsePage.getContent();

        return ResponseEntity.ok(ResponseDto.success(new MultiResponseDto<>(responses, responsePage)));
    }

    @Operation(summary = "quiz 생성 후 url 조회")
    @GetMapping("/endpoint/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> findEndpointByQuizId(@PathVariable("quiz-id") Long quizId) {
        String endpoint = quizFacade.findEndpointByQuizId(quizId);

        return ResponseEntity.ok(ResponseDto.success(endpoint));
    }

    @Operation(summary = "quiz 조회(참여자 전용)")
    @GetMapping("/form/{endpoint}")
    public ResponseEntity<ResponseDto<?>> findQuizByEndpoint(@PathVariable("endpoint") String endpoint) {

        QuizResponseDto response = quizFacade.findByEndPoint(endpoint);

        return ResponseEntity.ok(ResponseDto.success(response));
    }
}
