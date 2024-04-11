package com.quiz.domain.participantsinfo.repository.mongo;

import com.quiz.domain.participantsinfo.dto.ParticipantsRankResponseDto;
import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ParticipantInfoMongoTemplate {

    private final MongoTemplate mongoTemplate;

    public int countParticipantsByQuizIdAndSubmitResponses(Long quizId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("quiz_id").is(quizId)
                .and("submit_responses").is(true));
        long cnt = mongoTemplate.count(query, ParticipantInfo.class);

        return (int) cnt;
    }

    public int countParticipantsByQuizIdAndNotSubmitResponses(Long quizId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("quiz_id").is(quizId)
                .and("submit_responses").is(false));
        long cnt = mongoTemplate.count(query, ParticipantInfo.class);

        return (int) cnt;
    }

    @Transactional
    public ParticipantInfo save(ParticipantInfo participantInfo) {
        participantInfo = mongoTemplate.save(participantInfo, "participant_info");
        return participantInfo;
    }

    public Optional<ParticipantInfo> findByQuizIdAndUserId(Long quizId, Long userId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("quiz_id").is(quizId);
        criteria.and("user_id").is(userId);

        query.addCriteria(criteria);

        ParticipantInfo participantInfo = mongoTemplate.findOne(query, ParticipantInfo.class);
        return Optional.ofNullable(participantInfo);
    }

    public void update(Long quizId, Long userId, int participantCount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("quiz_id").is(quizId)
                .and("user_id").is(userId));

        Update update = new Update();
        update.set("submit_responses", true);
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

    public List<ParticipantInfo> findAll() {
        Query query = new Query();

        List<ParticipantInfo> participantInfos = mongoTemplate.findAll(ParticipantInfo.class);

        return participantInfos;
    }

    public void deleteAll() {
        Query query = new Query();
        mongoTemplate.remove(query, ParticipantInfo.class);
    }
}
