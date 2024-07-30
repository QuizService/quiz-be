package com.quiz.domain.quiz.entity;

import com.quiz.domain.quiz.dto.QuizRequestDto;
import com.quiz.exception.QuizException;
import com.quiz.utils.TimeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quiz.exception.code.QuizErrorCode.*;

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
    @Field(name = "user_id")
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
        LocalDateTime st = startDate == null ? null : TimeConverter.stringToLocalDateTime(startDate);
        LocalDateTime dt = startDate == null ? null : TimeConverter.stringToLocalDateTime(dueDate);

        validateStartDate(st);
        validateDueDateIsAfterStartDate(st, dt);

        this.idx = idx;
        this.title = title;
        this.capacity = capacity;
        this.userId = userId;
        this.startDate = st;
        this.dueDate = dt;
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

        validateStartDate(this.startDate);
        validateDueDateIsAfterStartDate(this.startDate, this.dueDate);

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

    private void generateRandomEndPoint() {
        String uuid = UUID.randomUUID().toString();
        this.endpoint = uuid.substring(0, 8);
    }

    private void validateStartDate(LocalDateTime startDate) {
        if(startDate == null) return;
        LocalDateTime today = LocalDateTime.now();
        if(today.isAfter(startDate)) {
            throw new QuizException(CANNOT_CREATE_AFTER_START_DATE);
        }
    }

    private void validateDueDateIsAfterStartDate(LocalDateTime startDate, LocalDateTime dueDate) {
        if(startDate == null || dueDate == null) return;
        if(startDate.isAfter(dueDate)) {
            throw new QuizException(START_DATE_CANNOT_BE_AFTER_DUE_DATE);
        }
    }

    public void setIdForTest(String id) {
        this.id = id;
    }

    public void setIdxForTest(Long idx) {
        this.idx = idx;
    }
}
