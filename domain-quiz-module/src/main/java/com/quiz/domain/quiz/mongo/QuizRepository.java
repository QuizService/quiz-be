package com.quiz.domain.quiz.mongo;

import com.quiz.domain.quiz.entity.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuizRepository extends MongoRepository<Quiz, String> {

    Optional<Quiz> findByIdx(Long idx);

    Optional<Quiz> findByEndpoint(String endpoint);
}
