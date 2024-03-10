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
@Document(collection = "question_response")
public class QuestionResponse {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "participant_info_id")
    private Long participantInfoId;

    @Field(name = "quiz_id")
    private Long quizId;

    @Field(value = "question_id")
    private Long questionId;

    private List<Integer> choices;

    private String answer;

    private Boolean isAnswers;

    private LocalDateTime created;

    @Builder
    public QuestionResponse(Long participantInfoId, Long quizId, Long questionId, List<Integer> choices, String answer, Boolean isAnswers) {
        this.participantInfoId = participantInfoId;
        this.quizId = quizId;
        this.questionId = questionId;
        this.choices = choices;
        this.answer = answer;
        this.isAnswers = (!choices.isEmpty() || answer != null) && isAnswers; //choices 와 answers 둘다 null인 경우 오답처리
        this.created = LocalDateTime.now();
    }
}
