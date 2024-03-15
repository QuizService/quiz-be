package com.quiz.domain.response.service;

import com.quiz.domain.response.mongo.ResponsesMongoTemplate;
import com.quiz.dto.responses.ResponsesRequestDto;
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

    public void saveResponses(String participantInfoId, List<ResponsesRequestDto> responses) {

    }
}
