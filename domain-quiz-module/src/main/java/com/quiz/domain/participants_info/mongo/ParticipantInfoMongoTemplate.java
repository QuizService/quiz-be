package com.quiz.domain.participants_info.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ParticipantInfoMongoTemplate {
    private final MongoTemplate mongoTemplate;


}
