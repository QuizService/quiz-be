package com.quiz.global.mock;

import com.quiz.domain.choice.dto.ChoicesRequestDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.domain.response.dto.ResponsesRequestDto;

import java.util.ArrayList;
import java.util.List;


public class TestDto {

    public static QuizRequestDto getQuizRequestDto() {
        return QuizRequestDto.builder()
                .title("test")
                .capacity(10)
                .startDate("2026-02-02 12:00:00")
                .dueDate("2026-02-05 12:00:00")
                .build();
    }

    public static QuestionsRequestDto getQuestionsReqDto() {
        return QuestionsRequestDto.builder()
                .title("question1")
                .score(10)
                .sequence(1)
                .questionType("M")
                .choices(getChoices())
                .build();
    }

    public static QuestionsRequestDto getUpdatedQuestionsReqDto() {
        return QuestionsRequestDto.builder()
                .title("updated question1")
                .score(20)
                .sequence(2)
                .questionType("S")
                .answer("updated answer")
                .build();
    }

    public static List<ChoicesRequestDto> getChoices() {
        List<ChoicesRequestDto> choicesRequestDtos = new ArrayList<>();
        for(int i = 1; i<=5; i++) {
            ChoicesRequestDto choicesRequestDto = ChoicesRequestDto.builder()
                    .sequence(i)
                    .title("choice + " + i)
                    .isAnswer(i == 4)
                    .build();
            choicesRequestDtos.add(choicesRequestDto);
        }
        return choicesRequestDtos;
    }

    public static ResponsesRequestDto getResponseRequestDto(String questionId) {
        return ResponsesRequestDto.builder()
                .questionId(questionId)
                .sequence(1)
                .choices(List.of(4))
                .build();
    }
}
