package com.quiz.domain.questions.service;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.mongo.QuestionsMongoTemplate;
import com.quiz.domain.questions.mongo.QuestionsRepository;
import com.quiz.dto.questions.QuestionsRequestDto;
import com.quiz.global.SequenceGenerator;
import com.quiz.global.exception.questions.QuestionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.quiz.global.exception.questions.enums.QuestionErrorType.QUESTION_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class QuestionService {
    private final QuestionsRepository questionsRepository;
    private final QuestionsMongoTemplate questionsMongoTemplate;
    private final SequenceGenerator sequenceGenerator;

    public Long save(List<Choices> choices, Answers answers, QuestionsRequestDto questionDto, Long quizId) {
        Long questionIdx = sequenceGenerator.generateSequence(Questions.SEQUENCE_NAME);
        Questions question = Questions.builder()
                .idx(questionIdx)
                .quizId(quizId)
                .title(questionDto.getTitle())
                .sequence(questionDto.getSequence())
                .score(questionDto.getScore())
                .choices(choices)
                .answers(answers)
                .build();
        question = questionsRepository.save(question);
        return question.getIdx();
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

    public void update(QuestionsRequestDto questionsDto, Long idx) {
        questionsMongoTemplate.updateQuestion(idx, questionsDto.getSequence(), questionsDto.getTitle(), questionsDto.getScore(), questionsDto.getQuestionType(), LocalDateTime.now());
    }

    public Questions findById(Long questionId) {
        return questionsRepository.findByIdx(questionId)
                .orElseThrow(() -> new QuestionException(QUESTION_NOT_FOUND));
    }

    public List<Questions> findByQuizId(Long quizId) {
        return questionsRepository.findAllByQuizId(quizId);
    }


    public void updateChoices(Long questionIdx, List<Choices> newChoices) {
        questionsRepository.updateChoices(questionIdx, newChoices);
    }

    public void updateAnswers(Long questionIdx,Answers newAnswers) {
        questionsRepository.updateAnswers(questionIdx,newAnswers);
    }
}
