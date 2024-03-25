package com.quiz.domain.participants_info.mongo;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantInfoRepository extends MongoRepository<ParticipantInfo, String> {
}
