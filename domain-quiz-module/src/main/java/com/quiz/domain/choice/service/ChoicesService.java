package com.quiz.domain.choice.service;

import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.choice.dto.ChoicesRequestDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.global.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ChoicesService {
    private final SequenceGenerator sequenceGenerator;

    //정답인 questionId 만 리턴
    public List<Choices> getChoicesFromDtos(QuestionsRequestDto questionDto, QuestionType questionType) {
        List<Choices> choices = new ArrayList<>();
        if(questionType.equals(QuestionType.MULTIPLE_CHOICE)) {
            List<ChoicesRequestDto> choicesDtoList = questionDto.getChoices();
            choices = choicesDtoList.stream()
                    .map(c -> Choices.builder()
                            .sequence(c.getSequence())
                            .title(c.getTitle())
                            .isAnswer(c.getIsAnswer())
                            .build())
                    .toList();
        }
        return choices;
    }

    public boolean isChoicesChanged(List<Choices> choices, List<Choices> newChoices) {
        if(choices.size() != newChoices.size()) {
            return true;
        }

        for (Choices newChoice : newChoices) {
            if(newChoice.getId() == null) {
                return true;
            }
        }

        choices.sort(Comparator.comparingInt(Choices::getSequence));
        newChoices.sort(Comparator.comparingInt(Choices::getSequence));

        for(int i = 0; i<choices.size(); i++) {
            Choices choice = choices.get(i);
            Choices newChoice = newChoices.get(i);

            if(!choice.isEqualsFromNew(newChoice)) {
                return true;
            }
        }
        return false;
    }
}
