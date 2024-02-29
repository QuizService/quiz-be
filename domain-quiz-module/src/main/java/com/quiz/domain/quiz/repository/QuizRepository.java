package com.quiz.domain.quiz.repository;

import com.quiz.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q.endpoint FROM Quiz q WHERE q.id = :quizId")
    Optional<String> findEndpointById(@Param("quizId") Long quizId);
}
