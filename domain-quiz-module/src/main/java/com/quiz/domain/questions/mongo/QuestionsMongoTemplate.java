package com.quiz.domain.questions.mongo;

import com.quiz.domain.questions.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class QuestionsMongoTemplate {

    private final MongoTemplate mongoTemplate;

    public void updateQuestion(Long idx,
                               Integer sequence,
                               String title,
                               Integer score,
                               String questionType,
                               LocalDateTime updated) {

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("idx").is(idx));

        update.set("sequence", sequence);
        update.set("title", title);
        update.set("score", score);
        update.set("questionType", questionType);
        update.set("updated", updated);

        mongoTemplate.updateMulti(query, update, Questions.class);
    }
}
