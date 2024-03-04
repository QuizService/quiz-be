package com.quiz.domain.quiz.entity;

import com.quiz.dto.quiz.QuizRequestDto;
import com.quiz.global.baseentity.BaseEntity;
import com.quiz.global.exception.quiz.QuizException;
import com.quiz.utils.TimeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quiz.global.exception.quiz.enums.QuizErrorType.CANNOT_UPDATE_AFTER_START_DATE;
import static com.quiz.global.exception.quiz.enums.QuizErrorType.MAXSCORE_CANNOT_BE_MINUS;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Quiz extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "max_score")
    private Integer maxScore;

    private Integer capacity;

    private String endpoint;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Builder
    public Quiz(String title, Integer capacity, Long userId, String startDate, String dueDate) {
        this.title = title;
        this.capacity = capacity;
        this.userId = userId;
        this.startDate = startDate == null ? LocalDateTime.now() : TimeConverter.stringToLocalDateTime(startDate);
        this.dueDate = dueDate == null ? null : TimeConverter.stringToLocalDateTime(dueDate);
        generateRandomEndPoint();
    }

    public void update(QuizRequestDto request) {
        if(startDate.isBefore(LocalDateTime.now())) {
            throw new QuizException(CANNOT_UPDATE_AFTER_START_DATE);
        }

        if(title != null) {
            this.title = request.getTitle();
        }
        if(capacity != null) {
            this.capacity = request.getCapacity();
        }
        if(startDate != null) {
            this.startDate = TimeConverter.stringToLocalDateTime(request.getStartDate());
        }
        if(dueDate != null) {
            this.dueDate = TimeConverter.stringToLocalDateTime(request.getDueDate());
        }
    }

    public void setMaxScore(Integer maxScore) {
        if(maxScore == null) {
            return;
        }
        if(maxScore < 0) {
            throw new QuizException(MAXSCORE_CANNOT_BE_MINUS);
        }
        this.maxScore = maxScore;
    }

    public void generateRandomEndPoint() {
        String uuid = UUID.randomUUID().toString();
        this.endpoint = uuid.substring(0, 8);
    }
}
