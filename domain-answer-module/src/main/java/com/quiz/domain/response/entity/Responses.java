package com.quiz.domain.response.entity;

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
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(collection = "responses")
public class Responses {
    @Transient
    public static final String SEQUENCE_NAME = "responses_sequence";

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "participant_info_id")
    private Long participantInfoId;

    private Long quizId;

    @Field(name = "question_responses")
    private List<QuestionResponse> questionResponses;

    private LocalDateTime created;

    @Builder
    public Responses(Long participantInfoId, Long quizId, List<QuestionResponse> questionResponses) {
        this.participantInfoId = participantInfoId;
        this.quizId = quizId;
        this.questionResponses = questionResponses;
        this.created = LocalDateTime.now();
    }
}
