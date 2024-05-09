package com.quiz.domain.participantsinfo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(collection = "participant_info")
public class ParticipantInfo {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(name = "quiz_id")
    private Long quizId;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "total_score")
    private Integer totalScore;

    private Integer number;

    @Field(name = "submit_responses")
    private boolean submitResponses;

    private LocalDateTime created;

    @Builder
    public ParticipantInfo(Long quizId, Long userId, Boolean submitResponses) {
        this.quizId = quizId;
        this.userId = userId;
        this.submitResponses = submitResponses != null && submitResponses;
        this.created = LocalDateTime.now();
    }

    @Builder(builderMethodName = "testBuilder", buildMethodName = "testBuild")
    public ParticipantInfo(String id, Long quizId, Long userId, Integer totalScore, Integer number, Boolean submitResponses) {
        this.id = id;
        this.quizId = quizId;
        this.userId = userId;
        this.totalScore = totalScore;
        this.number = number;
        this.submitResponses = submitResponses != null && submitResponses;
        this.created = LocalDateTime.now();
    }
}
