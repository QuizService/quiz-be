package com.quiz.domain.answers.service;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.choice.dto.ChoicesRequestDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.global.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AnswersService {
    private final SequenceGenerator sequenceGenerator;

    public Answers getAnswersFromDto(QuestionsRequestDto questionDto, QuestionType questionType) {
        List<Integer> answerChoiceIdxs = new ArrayList<>();
        String answer = null;

        if(questionType.equals(QuestionType.MULTIPLE_CHOICE)) {
            List<ChoicesRequestDto> choicesDtoList = questionDto.getChoices();
            answerChoiceIdxs = choicesDtoList.stream()
                    .filter(ChoicesRequestDto::getIsAnswer)
                    .map(ChoicesRequestDto::getSequence)
                    .toList();
        } else {
            answer = questionDto.getAnswer();
        }

        return Answers.builder()
                .shortAnswer(answer)
                .multipleChoiceAnswers(answerChoiceIdxs)
                .build();
    }

    public boolean isAnswersChanged(Answers answers, Answers newAnswers) {
        if(newAnswers.getId() == null) {
            return true;
        }

        return !answers.isEqualsToNew(newAnswers);

    }
}
