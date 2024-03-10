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

    private Integer score;

    private Integer number;

    private LocalDateTime created;

    @Builder
    public ParticipantInfo(Long quizId) {
        this.quizId = quizId;
        this.created = LocalDateTime.now();
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
