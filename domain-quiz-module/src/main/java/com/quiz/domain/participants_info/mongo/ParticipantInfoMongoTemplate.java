package com.quiz.domain.participants_info.mongo;

import com.quiz.domain.participants_info.dto.ParticipantsRankResponseDto;
import com.quiz.domain.participants_info.entity.ParticipantInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParticipantInfoMongoTemplate {
    private final MongoTemplate mongoTemplate;

    public int countParticipantsByQuizId(Long quizId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("quizId").is(quizId));

        long cnt = mongoTemplate.count(query, ParticipantInfo.class);

        return (int) cnt;
    }

    public String save(ParticipantInfo participantInfo) {
        ParticipantInfo savedParticipants = mongoTemplate.save(participantInfo);
        return savedParticipants.getId();
    }

    public Optional<ParticipantInfo> findByQuizIdAndUserId(Long quizId, Long userId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("quizId").is(quizId);
        criteria.and("userId").is(userId);

        query.addCriteria(criteria);

        ParticipantInfo participantInfo = mongoTemplate.findOne(query, ParticipantInfo.class);
        return Optional.of(participantInfo);
    }

    public void update(Long quizId, Long userId, int participantCount) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("quizId").is(quizId);
        criteria.and("userId").is(userId);
        query.addCriteria(criteria);

        Update update = new Update();
        update.set("number", participantCount);

        mongoTemplate.updateFirst(query, update, ParticipantInfo.class);
    }


    public void updateScore(String participantId, Integer score) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(participantId));

        Update update = new Update();
        update.set("total_score", score);

        mongoTemplate.updateFirst(query, update, ParticipantInfo.class);
    }

    public List<ParticipantInfo> findAllByQuizIdOrderByNumber(Long quizId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("quiz_id").is(quizId));
        query.with(Sort.by(Sort.Direction.ASC, "number"));

        return mongoTemplate.find(query, ParticipantInfo.class, "participant_info");
    }

    public List<ParticipantsRankResponseDto> findParticipantsRankResponsesByQuizIdOrderByNumber(Long quizId) {
        Query query = new Query();
        query.fields().include("id").include("userId").include("number").include("totalScore");
        query.addCriteria(Criteria.where("quiz_id").is(quizId));
        query.with(Sort.by(Sort.Direction.ASC, "number"));

        return mongoTemplate.find(query, ParticipantsRankResponseDto.class, "participant_info");
    }
}
