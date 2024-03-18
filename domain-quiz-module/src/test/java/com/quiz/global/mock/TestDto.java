package com.quiz.global.mock;

import com.quiz.domain.choice.dto.ChoicesRequestDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;

import java.util.ArrayList;
import java.util.List;

public class TestDto {

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
}
