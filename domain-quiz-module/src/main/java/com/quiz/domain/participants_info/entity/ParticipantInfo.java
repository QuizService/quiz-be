package com.quiz.domain.participants_info.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Document(collection = "participant_info")
public class ParticipantInfo {


    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    // 보조 키
    private Long idx;

    @Field(name = "quiz_id")
    private Long quizId;

    @Field(name = "use_id")
    private Long userId;

    @Field(name = "total_score")
    private Integer totalScore;

    private Integer number;

    private LocalDateTime created;

    @Builder
    public ParticipantInfo(Long quizId, Long userId) {
        this.quizId = quizId;
        this.userId = userId;
        this.created = LocalDateTime.now();
    }

    @Builder(builderMethodName = "testBuilder", buildMethodName = "testBuild")
    public ParticipantInfo(String id, Long idx, Long quizId, Long userId, Integer totalScore, Integer number, LocalDateTime created) {
        this.id = id;
        this.idx = idx;
        this.quizId = quizId;
        this.userId = userId;
        this.totalScore = totalScore;
        this.number = number;
        this.created = LocalDateTime.now();
    }
}
