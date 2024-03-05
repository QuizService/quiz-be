package com.quiz.domain.choice.entity;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choices {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    private Integer sequence;

    private String title;

    private Boolean isAnswer;

    private LocalDateTime created;

    @Builder
    public Choices(Integer sequence, String title, Boolean isAnswer) {
        this.sequence = sequence;
        this.title = title;
        this.isAnswer = isAnswer;
        this.created = LocalDateTime.now();
    }

    public boolean isEqualsFromNew(Object obj) {
        if(!(obj instanceof Choices choices)) return false;
        return choices.getIsAnswer().equals(this.isAnswer)
                && choices.getSequence().equals(this.sequence)
                && choices.getTitle().equals(this.title);
    }

}
