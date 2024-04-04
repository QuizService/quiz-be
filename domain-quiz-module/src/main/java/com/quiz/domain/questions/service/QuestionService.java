package com.quiz.domain.questions.service;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.repository.mongo.QuestionsMongoTemplate;
import com.quiz.domain.questions.repository.mongo.QuestionsRepository;
import com.quiz.domain.choice.dto.ChoicesResponseDto;
import com.quiz.domain.questions.dto.QuestionsAnswerDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.questions.dto.QuestionsResponseDto;
import com.quiz.global.SequenceGenerator;
import com.quiz.global.exception.questions.QuestionException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.quiz.global.exception.questions.enums.QuestionErrorType.QUESTION_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(value = "mongoTx")
@Service
public class QuestionService {
    private final QuestionsRepository questionsRepository;
    private final QuestionsMongoTemplate questionsMongoTemplate;

    public String save(List<Choices> choices, Answers answers, QuestionsRequestDto questionDto, Long quizId) {
        Questions question = Questions.builder()
                .quizId(quizId)
                .title(questionDto.getTitle())
                .sequence(questionDto.getSequence())
                .score(questionDto.getScore())
                .questionType(questionDto.getQuestionType())
                .choices(choices)
                .answers(answers)
                .build();
        question = questionsRepository.save(question);
        return question.getId();
    }


    public boolean questionTypeHasChanged(QuestionsRequestDto questionDto) {
        Questions questions = findById(questionDto.getQuestionId());
        QuestionType updatedQuestionType = QuestionType.findByInitial(questionDto.getQuestionType());
        boolean isQuestionTypeChanged = false;
        if(questions.getQuestionType().equals(updatedQuestionType)) {
            isQuestionTypeChanged = true;
        }
        updateQuestions(questionDto, questions);
        return isQuestionTypeChanged;
    }

    public void updateQuestions(QuestionsRequestDto questionDto, Questions questions) {
        questions.update(questionDto);
        questionsRepository.save(questions);
    }

    public void update(QuestionsRequestDto questionsDto, String questionId) {
        questionsMongoTemplate.updateQuestion(questionId, questionsDto.getSequence(), questionsDto.getTitle(), questionsDto.getScore(), questionsDto.getQuestionType(), LocalDateTime.now());
    }

    public Questions findById(String questionId) {
        return questionsRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QUESTION_NOT_FOUND));
    }

    public List<Questions> findByQuizId(Long quizId) {
        return questionsRepository.findAllByQuizId(quizId);
    }


    public void updateChoices(String questionId, List<Choices> newChoices) {
        questionsRepository.updateChoices(questionId, newChoices);
    }

    public void updateAnswers(String questionId,Answers newAnswers) {
        questionsRepository.updateAnswers(questionId,newAnswers);
    }

    public Page<QuestionsResponseDto> findResponseByQuizId(Long quizId, int page, int size) {
        Page<Questions> questions = questionsMongoTemplate.findPageByQuizId(quizId, page, size);
        return questions.map(question -> QuestionsResponseDto.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .score(question.getScore())
                .questionType(question.getQuestionType().getValue())
                .choicesResponseDtos(toDto(question.getChoices()))
                .build());
    }

    public Page<QuestionsResponseDto> findResponseByEndpoint(Long quizId, int page, int size) {
        Page<Questions> questions = questionsMongoTemplate.findPageByQuizId(quizId, page, size);
        return questions.map(question -> QuestionsResponseDto.builder()
                .questionId(question.getId())
                .title(question.getTitle())
                .score(question.getScore())
                .questionType(question.getQuestionType().getValue())
                .choicesResponseDtos(toDto(question.getChoices()))
                .build());
    }

    private List<ChoicesResponseDto> toDto(List<Choices> choices) {
        return choices.stream().map(choice -> ChoicesResponseDto.builder()
                .seq(choice.getSequence())
                .title(choice.getTitle())
                .build()).toList();
    }

    //get Answers from questions
    public List<QuestionsAnswerDto> findAnswersByQuestionsInQuiz(Long quizId) {
        List<Questions> questions = questionsMongoTemplate.findAllByQuizIdOrderBySequence(quizId);
        return questions.stream()
            .map(q -> QuestionsAnswerDto.builder()
                    .id(q.getId())
                    .sequence(q.getSequence())
                    .questionType(q.getQuestionType())
                    .score(q.getScore())
                    .choices(q.getAnswers().getMultipleChoiceAnswers())
                    .answer(q.getAnswers().getShortAnswer())
                    .build())
                .toList();
    }

    //for test
    public void deleteAll() {
        questionsRepository.deleteAll();
    }
}
