package com.quiz.domain.answers.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class Answers {

    @Transient
    public static final String SEQUENCE_NAME = "answers_sequence";


    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field
    private List<Integer> multipleChoiceAnswers;

    @Field
    private String shortAnswer;

    private LocalDateTime created;

    @Builder
    public Answers(String shortAnswer, List<Integer> multipleChoiceAnswers) {
        this.multipleChoiceAnswers = multipleChoiceAnswers == null ? new ArrayList<>() : multipleChoiceAnswers;
        this.shortAnswer = shortAnswer;
        this.created = LocalDateTime.now();
    }

    public void update(String answers) {
        if(answers != null) {
            this.shortAnswer = answers;
        }
    }

    public boolean isEqualsToNew(Answers answers) {
        return answers.getMultipleChoiceAnswers().equals(this.multipleChoiceAnswers)
                && answers.getShortAnswer().equals(this.shortAnswer);
    }

}
