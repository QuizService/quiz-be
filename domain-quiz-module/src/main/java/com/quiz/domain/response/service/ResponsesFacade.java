package com.quiz.domain.response.service;

import com.quiz.domain.participants_info.service.ParticipantInfoService;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.service.QuestionService;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.domain.questions.dto.QuestionsAnswerDto;
import com.quiz.domain.response.dto.ResponsesRequestDto;
import com.quiz.domain.response.entity.Responses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ResponsesFacade {
    private final ParticipantInfoService participantInfoService;
    private final QuestionService questionService;
    private final ResponsesService responsesService;
    private final QuizService quizService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void saveResponse(Long quizId, Long userId, List<ResponsesRequestDto> responses) {
        Integer quizCapacity = quizService.findById(quizId)
                        .getCapacity();
        if(quizCapacity > 0) {
            participantInfoService.updateFcFs(quizId, userId, quizCapacity);
        }
        String participantInfoId = participantInfoService.findByQuizIdAndUserId(quizId, userId)
                .getId();

        Map<String, Object> params = new HashMap<>();
        params.put("participantInfoId", participantInfoId);
        params.put("responses", responses);
        params.put("quizId", quizId);
        //event 로 저장과정 분리
        applicationEventPublisher.publishEvent(params);
    }

    /*
    * 채점 방법
    * 1. 답변 저장 후 바로 실행 ---> 일단 이걸로 ㄱㄱ
    * 2. 답변을 모았다가 한번에 실행
    * 3. 정답을 다른 캐시 저장소에 저장하여 비교
    * */
    public void calculateScoreAndSaveResponse(Long quizId, List<ResponsesRequestDto> responses, String participantInfoId) {
        List<QuestionsAnswerDto> answers = questionService.findAnswersByQuestionsInQuiz(quizId);
        Map<String, QuestionsAnswerDto> answerDtoMap = answers.stream()
                .collect(
                        Collectors.toMap(
                                QuestionsAnswerDto::getId,
                                i2 -> i2

                        )
                );
        List<Responses> responsesList = responses
                .stream()
                .map(r -> Responses.builder()
                        .participantInfoId(participantInfoId)
                        .quizId(quizId)
                        .questionId(r.getQuestionId())
                        .choices(r.getChoices())
                        .answer(r.getAnswer())
                        .isAnswers(isResponseCorrect(r, answerDtoMap))
                        .build())
                .toList();
        responsesService.saveResponses(responsesList);
    }

    public boolean isResponseCorrect(ResponsesRequestDto responses, Map<String, QuestionsAnswerDto> answerDtoMap) {
        String questionId = responses.getQuestionId();
        QuestionsAnswerDto questionsAnswerDto = answerDtoMap.get(questionId);
        if(questionsAnswerDto.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
            if(responses.getChoices().isEmpty()) return false;
            List<Integer> choices = responses.getChoices();
            choices.sort(Comparator.naturalOrder());

            List<Integer> answerChoices = questionsAnswerDto.getChoices();
            answerChoices.sort(Comparator.naturalOrder());

            return answerChoices.equals(choices);
        } else {
            String res = responses.getAnswer();
            res = res.trim();

            return questionsAnswerDto.getAnswer().equals(res);
        }
    }
}