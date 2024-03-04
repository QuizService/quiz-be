package com.quiz.domain.questions.mock;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.entity.Questions;

import java.util.ArrayList;
import java.util.List;

public class TestEntities {

    public static final Long IDX = 1L;
    public static final Long QUIZ_ID = 1L;
    public static final int ANSWER_NUMBER = 4;
    public static final List<Integer> UPDATE_ANSWER_NUMBER = List.of(1,4);

    public static List<Choices> getChoices() {
        List<Choices> choicesList = new ArrayList<>();
        for(int i = 1; i<=5; i++) {
            Choices choice = Choices.builder()
                    .idx((long)i)
                    .sequence(i)
                    .title("choice " + i)
                    .isAnswer(i == ANSWER_NUMBER)
                    .build();
            choicesList.add(choice);
        }
        return choicesList;
    }

    public static Answers getAnswers() {
        return Answers.builder()
                .idx(IDX)
                .multipleChoiceAnswers(List.of(ANSWER_NUMBER))
                .build();
    }

    public static Questions getQuestions() {
        return Questions.builder()
                .idx(IDX)
                .quizId(QUIZ_ID)
                .sequence(1)
                .title("question 1")
                .score(10)
                .questionType("M")
                .choices(getChoices())
                .answers(getAnswers())
                .build();
    }

    // update entities
    public static List<Choices> getMultipleUpdateChoices() {
        List<Choices> choicesList = new ArrayList<>();
        for(int i = 1; i<=5; i++) {
            Choices choice = Choices.builder()
                    .idx((long)i + 5)
                    .sequence(i)
                    .title("updatechoice " + i)
                    .isAnswer(UPDATE_ANSWER_NUMBER.contains(i))
                    .build();
            choicesList.add(choice);
        }
        return choicesList;
    }

    public static Answers getUpdatedAnswers() {
        return Answers.builder()
                .idx(IDX + 1L)
                .multipleChoiceAnswers(UPDATE_ANSWER_NUMBER)
                .build();
    }
}
