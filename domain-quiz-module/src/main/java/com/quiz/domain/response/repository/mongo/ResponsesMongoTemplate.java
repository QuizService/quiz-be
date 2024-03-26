package com.quiz.domain.response.repository.mongo;

import com.quiz.domain.response.entity.Responses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResponsesMongoTemplate {
    private final MongoTemplate mongoTemplate;

    public void saveAll(List<Responses> questionResponse) {
        mongoTemplate.insert(questionResponse, Responses.class);
    }

    public void updateAll(List<Responses> questionResponse) {

    }

    public List<Responses> findAllByQuizId(Long quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("quiz_id").is(quizId));
        query.with(Sort.by(Sort.Direction.ASC,"number"));

        return mongoTemplate.find(query, Responses.class, "responses");
    }
}
