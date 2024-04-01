package com.quiz.domain.response.repository.mongo;

import com.quiz.domain.response.entity.Responses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsesMongoRepository extends MongoRepository<Responses, String> {
}
