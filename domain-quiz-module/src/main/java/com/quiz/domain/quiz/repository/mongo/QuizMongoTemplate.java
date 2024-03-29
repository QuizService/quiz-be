package com.quiz.domain.quiz.repository.mongo;

import com.quiz.domain.quiz.dto.QuizResponseDto;
import com.quiz.domain.quiz.entity.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        update.set("capacity", quiz.getCapacity());
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

    public Page<Quiz> findQuizByUserId(Long userId, Pageable pageable) {
        Criteria criteria = Criteria.where("user_id").is(userId);

        AggregationOperation match = Aggregation.match(criteria);
        AggregationOperation sort = Aggregation.sort(pageable.getSort());
        AggregationOperation skip = Aggregation.skip(pageable.getOffset());
        AggregationOperation limit = Aggregation.limit(pageable.getPageSize());

        Aggregation aggregation = Aggregation.newAggregation(match, sort, skip, limit);

        AggregationResults<Quiz> result = mongoTemplate.aggregate(aggregation, Quiz.class, Quiz.class);
        List<Quiz> quizzes = result.getMappedResults();

        return PageableExecutionUtils.getPage(quizzes, pageable,
                () -> mongoTemplate
                        .count(Query.query(Criteria.where("user_id")
                .is(userId)), Quiz.class));

    }

}
