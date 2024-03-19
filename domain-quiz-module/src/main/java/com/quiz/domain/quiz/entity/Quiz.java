package com.quiz.domain.quiz.entity;

import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.global.baseentity.QuizBaseEntity;
import com.quiz.global.exception.quiz.QuizException;
import com.quiz.utils.TimeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quiz.global.exception.quiz.enums.QuizErrorType.CANNOT_UPDATE_AFTER_START_DATE;
import static com.quiz.global.exception.quiz.enums.QuizErrorType.MAXSCORE_CANNOT_BE_MINUS;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class Quiz {
    @Transient
    public static final String SEQUENCE_NAME = "quiz_sequence";

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    private Long idx;

    private Long userId;


    private String title;

    @Field(name = "max_score")
    private Integer maxScore;

    private Integer capacity;

    private String endpoint;

    @Field(name = "start_date")
    private LocalDateTime startDate;

    @Field(name = "due_date")
    private LocalDateTime dueDate;

    @Field
    private LocalDateTime created;

    @Field
    private LocalDateTime updated;

    @Builder
    public Quiz(Long idx, String title, Integer capacity, Long userId, String startDate, String dueDate) {
        this.idx = idx;
        this.title = title;
        this.capacity = capacity;
        this.userId = userId;
        this.startDate = startDate == null ? LocalDateTime.now() : TimeConverter.stringToLocalDateTime(startDate);
        this.dueDate = dueDate == null ? null : TimeConverter.stringToLocalDateTime(dueDate);
        generateRandomEndPoint();
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
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
        this.updated = LocalDateTime.now();
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
