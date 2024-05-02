package com.quiz.domain.questions.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.Document;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCountDto {
    private Long quizId;
    private int questionCnt;

    public QuestionCountDto(Document document) {
        this.quizId = document.getLong("_id");
        this.questionCnt = document.getInteger("questionCnt");
    }
}
