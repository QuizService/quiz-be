package com.quiz.domain.response.mongo;

import com.quiz.domain.response.entity.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionResponseMongoTemplate {
    private final MongoTemplate mongoTemplate;

    public void saveAll(List<QuestionResponse> questionResponses) {
        mongoTemplate.insert(questionResponses, QuestionResponse.class);
    }

    public void updateAll(List<QuestionResponse> questionResponses) {

    }

}
