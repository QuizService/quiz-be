package com.quiz.global.mock;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.quiz.entity.Quiz;

import java.util.ArrayList;
import java.util.List;

public class TestEntities {

    public static final Long QUIZ_ID = 1L;
    public static final int ANSWER_NUMBER = 4;
    public static final List<Integer> UPDATE_ANSWER_NUMBER = List.of(1, 4);

    public static Quiz getQuiz() {
        return Quiz.builder()
                .idx(1L)
                .userId(1L)
                .title("quizTitle")
                .capacity(100)
                .startDate("2024-10-10 00:00:00")
                .dueDate("2024-10-10 00:00:00")
                .build();
    }

    public static Quiz getUpdatedQuiz() {
        return Quiz.builder()
                .idx(1L)
                .userId(1L)
                .title("updated quizTitle")
                .capacity(110)
                .startDate("2024-12-10 00:00:00")
                .dueDate("2024-12-10 00:00:00")
                .build();
    }

    public static List<Choices> getChoices() {
        List<Choices> choicesList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Choices choice = Choices.builder()
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
                .multipleChoiceAnswers(List.of(ANSWER_NUMBER))
                .build();
    }

    public static Questions getQuestions() {
        return Questions.builder()
                .quizId(QUIZ_ID)
                .sequence(1)
                .title("question 1")
                .score(10)
                .questionType("M")
                .choices(getChoices())
                .answers(getAnswers())
                .build();
    }

    public static Questions getQuestions(Long quizId) {
        return Questions.builder()
                .quizId(quizId)
                .sequence(1)
                .title("question 1")
                .score(10)
                .questionType("M")
                .choices(getChoices())
                .answers(getAnswers())
                .build();
    }

    public static Questions getUpdatedQuestions() {
        return Questions.builder()
                .quizId(QUIZ_ID)
                .sequence(1)
                .title("question 1")
                .score(10)
                .questionType("M")
                .choices(getMultipleUpdateChoices())
                .answers(getNewMultipleAnswers())
                .build();
    }

    public static List<ParticipantInfo> getParticipantInfos() {
        List<ParticipantInfo> participantInfos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ParticipantInfo participantInfo = ParticipantInfo.testBuilder()
                    .quizId(1L)
                    .userId((long) i)
                    .totalScore(i)
                    .number(i)
                    .submitResponses(true)
                    .testBuild();
            participantInfos.add(participantInfo);
        }
        return participantInfos;
    }

    // update entities
    public static List<Choices> getMultipleUpdateChoices() {
        List<Choices> choicesList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Choices choice = Choices.builder()
                    .sequence(i)
                    .title("updatechoice " + i)
                    .isAnswer(UPDATE_ANSWER_NUMBER.contains(i))
                    .build();
            choicesList.add(choice);
        }
        return choicesList;
    }

    public static Answers getNewMultipleAnswers() {
        return Answers.builder()
                .multipleChoiceAnswers(UPDATE_ANSWER_NUMBER)
                .build();
    }

    public static Answers getNewShortAnswers() {
        return Answers.builder()
                .shortAnswer("answers")
                .build();
    }
}
