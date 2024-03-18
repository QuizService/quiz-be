package com.quiz.domain.response.service;

import com.quiz.domain.response.entity.Responses;
import com.quiz.domain.response.mongo.ResponsesMongoTemplate;
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

    public void saveResponses(List<Responses> responses) {
        responsesMongoTemplate.saveAll(responses);
    }


}
