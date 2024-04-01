package com.quiz.domain.quiz.repository;

import com.quiz.TestConfiguration;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.repository.mongo.QuizRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class})
public class QuizRepositoryTest {

    @Autowired
    QuizRepository quizRepository;

    @Test
    @Transactional
    void testTx() {
        Quiz quiz = Quiz.builder()
                .idx(14L)
                .title("throw err")
                .capacity(10)
                .userId(4L)
                .startDate("2024-10-01 00:00:00")
                .dueDate("2024-10-02 00:00:00")
                .build();
        quizRepository.save(quiz);
        throw new RuntimeException("throw error!!");
    }

}
