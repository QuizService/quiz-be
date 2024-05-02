package com.quiz.domain.answers.entity;

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
public class Answers {

    @Transient
    public static final String SEQUENCE_NAME = "answers_sequence";

    private Long idx;

    @Field
    private List<Integer> multipleChoiceAnswers;

    @Field
    private String shortAnswer;

    private LocalDateTime created;

    @Builder
    public Answers(Long idx, String shortAnswer, List<Integer> multipleChoiceAnswers) {
        this.idx = idx;
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
        if(answers.getShortAnswer() == null && this.shortAnswer == null) {
            return answers.getMultipleChoiceAnswers().equals(this.multipleChoiceAnswers);
        }

        return answers.getMultipleChoiceAnswers().equals(this.multipleChoiceAnswers)
                && answers.getShortAnswer().equals(this.shortAnswer);
    }

}
