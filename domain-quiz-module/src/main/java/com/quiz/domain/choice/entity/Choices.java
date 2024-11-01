package com.quiz.domain.choice.entity;

import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choices {
    @Transient
    public static final String SEQUENCE_NAME = "choices_sequence";
    private Long idx;

    private Integer sequence;

    private String title;

    private Boolean isAnswer;

    private LocalDateTime created;

    @Builder
    public Choices(Long idx, Integer sequence, String title, Boolean isAnswer) {
        this.idx = idx;
        this.sequence = sequence;
        this.title = title;
        this.isAnswer = isAnswer;
        this.created = LocalDateTime.now();
    }

    public boolean isEqualsFromNew(Object obj) {
        if (!(obj instanceof Choices choices)) return false;
        return choices.getIsAnswer().equals(this.isAnswer)
                && choices.getSequence().equals(this.sequence)
                && choices.getTitle().equals(this.title);
    }

}
