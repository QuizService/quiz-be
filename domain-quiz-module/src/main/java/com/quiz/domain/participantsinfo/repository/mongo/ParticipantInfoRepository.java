package com.quiz.domain.participantsinfo.repository.mongo;

import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantInfoRepository extends MongoRepository<ParticipantInfo, String> {
}
