package com.quiz.domain.response.service;

import com.quiz.domain.response.entity.Responses;
import com.quiz.domain.response.repository.mongo.ResponsesMongoRepository;
import com.quiz.domain.response.repository.mongo.ResponsesMongoTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ResponsesService {
    private final ResponsesMongoTemplate responsesMongoTemplate;
    private final ResponsesMongoRepository responsesMongoRepository;

    public void saveResponses(List<Responses> responses) {
        responsesMongoRepository.saveAll(responses);
    }


}
