package com.quiz.domain.questions.repository.mongo;

import com.quiz.domain.answers.entity.Answers;
import com.quiz.domain.choice.entity.Choices;
import com.quiz.domain.questions.dto.QuestionCountDto;
import com.quiz.domain.questions.dto.QuestionsRequestDto;
import com.quiz.domain.questions.entity.QuestionType;
import com.quiz.domain.questions.entity.Questions;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class QuestionsMongoTemplate {

    private final MongoTemplate mongoTemplate;

    public void updateQuestion(String id,
                               QuestionsRequestDto questionsDto,
                               LocalDateTime updated) {

        Query query = new Query();
        Update update = new Update();

        query.addCriteria(Criteria.where("_id").is(id));

        update.set("sequence", questionsDto.getSequence());
        update.set("title", questionsDto.getTitle());
        update.set("score", questionsDto.getScore());
        update.set("questionType", QuestionType.findByInitial(questionsDto.getQuestionType()));
        update.set("updated", updated);

        mongoTemplate.updateFirst(query, update, Questions.class);
    }

    public void updateChoices(String questionId, List<Choices> choices) {
        Query query = new Query();
        Update update = new Update();
        update.set("choices", choices);

        query.addCriteria(Criteria.where("_id").is(questionId));
        mongoTemplate.updateFirst(query, update, Questions.class);
    }

    public void updateAnswers(String questionId, Answers answers) {
        Query query = new Query();
        Update update = new Update();
        update.set("answers", answers);

        query.addCriteria(Criteria.where("_id").is(questionId));
        mongoTemplate.updateFirst(query, update, Questions.class);
    }

    public Page<Questions> findPageByQuizId(Long quizId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "sequence");

        Query query = new Query();
        query.addCriteria(Criteria.where("quiz_id").is(quizId));

        List<Questions> filteredQuestions = mongoTemplate.find(query, Questions.class, "questions");

        return PageableExecutionUtils.getPage(filteredQuestions,
                pageable,
                () -> mongoTemplate.count(query.skip(-1).limit(-1), Questions.class));
    }

    public List<Questions> findPageByQuizId(Long quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("quiz_id").is(quizId))
                .with(Sort.by(new Sort.Order(Sort.Direction.ASC, "sequence")));

        return mongoTemplate.find(query, Questions.class, "questions");
    }

    public List<Questions> findAllByQuizIdOrderBySequence(Long quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("quiz_id").is(quizId));
        query.with(Sort.by(Sort.Direction.ASC, "sequence"));

        return mongoTemplate.find(query, Questions.class, "questions");
    }

    public List<QuestionCountDto> findQuestionCntsByQuizId(List<Long> quizIds) {
        MatchOperation matchOperation =
                Aggregation.match(Criteria.where("quiz_id").in(quizIds));

        GroupOperation groupOperation =
                Aggregation.group("quiz_id")
                        .count().as("questionCnt");


        Aggregation aggregation =
                Aggregation.newAggregation(matchOperation, groupOperation);
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "questions", Document.class);
        List<Document> documentList = results.getMappedResults();
        return documentList.stream()
                .map(QuestionCountDto::new)
                .toList();
    }

    public void deleteQuestionsByQuestionIds(List<String> questionIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(questionIds));

        mongoTemplate.remove(query, Questions.class);
    }
}
