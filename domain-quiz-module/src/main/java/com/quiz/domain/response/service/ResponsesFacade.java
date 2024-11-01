package com.quiz.domain.response.service;

import com.quiz.domain.participantsinfo.service.ParticipantInfoService;
import com.quiz.domain.questions.dto.QuestionsAnswerDto;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.service.QuestionService;
import com.quiz.domain.quiz.service.QuizService;
import com.quiz.domain.response.dto.ResponsesRequestDto;
import com.quiz.domain.response.entity.Responses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(value = "mongoTx")
@RequiredArgsConstructor
@Service
public class ResponsesFacade {
    private final ParticipantInfoService participantInfoService;
    private final QuestionService questionService;
    private final ResponsesService responsesService;
    private final QuizService quizService;

    /*
     * 채점 방법
     * 1. 답변 저장 후 바로 실행 ---> 일단 이걸로 ㄱㄱ
     * 2. 답변을 모았다가 한번에 실행
     * 3. 정답을 다른 캐시 저장소에 저장하여 비교
     * */
    public int calculateScoreAndSaveResponse(Long quizId, List<ResponsesRequestDto> responses, String participantInfoId) {
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
                        .choices(r.getChoices() != null ? r.getChoices() : new ArrayList<>())
                        .answer(r.getAnswer())
                        .isAnswers(isResponseCorrect(r, answerDtoMap))
                        .build())
                .toList();
        responsesService.saveResponses(responsesList);

        int totalScore = responsesList.stream()
                .filter(Responses::getIsAnswers)
                .mapToInt(r -> answerDtoMap.get(r.getQuestionId()).getScore())
                .sum();
        participantInfoService.updateTotalScore(participantInfoId, totalScore);
        log.info("totalScore = {}", totalScore);
        return totalScore;

    }

    public boolean isResponseCorrect(ResponsesRequestDto responses, Map<String, QuestionsAnswerDto> answerDtoMap) {
        String questionId = responses.getQuestionId();
        QuestionsAnswerDto questionsAnswerDto = answerDtoMap.get(questionId);
        if (questionsAnswerDto.getQuestionType().equals(QuestionType.MULTIPLE_CHOICE)) {
            if (responses.getChoices().isEmpty()) return false;
            List<Integer> choices = responses.getChoices();
            choices = new ArrayList<>(choices);
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
