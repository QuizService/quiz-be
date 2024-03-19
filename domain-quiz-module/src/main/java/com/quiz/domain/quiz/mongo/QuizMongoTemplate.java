package com.quiz.domain.quiz.mongo;

import com.quiz.domain.quiz.entity.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class QuizMongoTemplate {
    private final MongoTemplate mongoTemplate;

    public void update(Quiz quiz) {
        String id = quiz.getId();

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("id").is(id));

        update.set("title", quiz.getTitle());
        update.set("max_score", quiz.getMaxScore());
        update.set("start_date", quiz.getStartDate());
        update.set("due_date", quiz.getDueDate());
        update.set("updated", quiz.getUpdated());

        mongoTemplate.updateFirst(query, update, Quiz.class);
    }

    public void updateMaxScore(Quiz quiz) {
        String id = quiz.getId();

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("id").is(id));

        update.set("max_score", quiz.getMaxScore());

        mongoTemplate.updateFirst(query, update, Quiz.class);
    }

}
