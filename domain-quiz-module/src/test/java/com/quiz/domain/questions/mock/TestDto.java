package com.quiz.domain.questions.mock;

import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.dto.choices.ChoicesRequestDto;
import com.quiz.dto.questions.QuestionsRequestDto;

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
