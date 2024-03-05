package com.quiz.domain.questions.entity;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.dto.questions.QuestionsRequestDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "questions")
public class Questions {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "quiz_id")
    private Long quizId;

    @Field
    private int sequence;

    @Field
    private String title;

    @Field
    private Integer score;

    @Field
    private QuestionType questionType;

    @Field
    private List<Choices> choices;

    @Field
    private Answers answers;

    @Field
    private LocalDateTime created;

    @Field
    private LocalDateTime updated;

    @Builder
    public Questions(Long quizId, int sequence, String title, Integer score, String questionType, List<Choices> choices, Answers answers) {
        this.quizId = quizId;
        this.sequence = sequence;
        this.title = title;
        this.score = score;
        this.questionType = QuestionType.findByInitial(questionType);
        this.choices = choices == null ? new ArrayList<>() : choices;
        this.answers = answers;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public void update(QuestionsRequestDto questionDto) {
        if(questionDto.getTitle() != null) {
            this.title = questionDto.getTitle();
        }
        if(questionDto.getScore() != null) {
            this.score = questionDto.getScore();
        }
        if(questionDto.getSequence() != null) {
            this.sequence = questionDto.getSequence();
        }
        if(questionDto.getQuestionType() != null) {
            this.questionType = QuestionType.findByInitial(questionDto.getQuestionType());
        }
        this.updated = LocalDateTime.now();
    }

    public void updateChoices(List<Choices> choices) {
        this.choices = choices == null ? new ArrayList<>() : choices;
    }

    public void updateAnswers(Answers answers) {
        this.answers = answers;
    }
}
