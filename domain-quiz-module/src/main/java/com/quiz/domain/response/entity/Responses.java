package com.quiz.domain.response.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "responses")
public class Responses {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "participant_info_id")
    private String participantInfoId;

    @Field(name = "quiz_id")
    private Long quizId;

    @Field(value = "question_id")
    private String questionId;

    private Integer sequence;

    private List<Integer> choices;

    private String answer;

    private Boolean isAnswers;

    private LocalDateTime created;

    @Builder
    public Responses(String participantInfoId, Long quizId, String questionId, Integer sequence, List<Integer> choices, String answer, Boolean isAnswers) {
        this.participantInfoId = participantInfoId;
        this.quizId = quizId;
        this.questionId = questionId;
        this.sequence = sequence;
        this.choices = choices;
        this.answer = answer;
        this.isAnswers = (!choices.isEmpty() || answer != null) && isAnswers; //choices 와 answers 둘다 null인 경우 오답처리
        this.created = LocalDateTime.now();
    }
}
