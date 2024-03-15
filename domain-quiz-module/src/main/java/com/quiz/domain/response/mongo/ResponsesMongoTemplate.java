package com.quiz.domain.response.mongo;

import com.quiz.domain.response.entity.Responses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResponsesMongoTemplate {
    private final MongoTemplate mongoTemplate;

    public void saveAll(List<Responses> questionRespons) {
        mongoTemplate.insert(questionRespons, Responses.class);
    }

    public void updateAll(List<Responses> questionRespons) {

    }

}
