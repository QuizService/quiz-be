package com.quiz.domain.participants_info.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ParticipantInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_id")
    private Long quizId;

    private Integer score;

    private Integer number;

    private LocalDateTime created;

    @Builder
    public ParticipantInfo(Long idx, Long quizId) {
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
