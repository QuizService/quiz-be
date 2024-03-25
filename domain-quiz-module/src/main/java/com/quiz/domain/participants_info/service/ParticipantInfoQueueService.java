package com.quiz.domain.participants_info.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ParticipantInfoQueueService {
    private final RedisTemplate<String, Object> redisTemplate;


}
