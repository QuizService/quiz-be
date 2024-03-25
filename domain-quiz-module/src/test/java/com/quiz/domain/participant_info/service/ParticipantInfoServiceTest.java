package com.quiz.domain.participant_info.service;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import com.quiz.domain.participants_info.service.ParticipantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestConfiguration
@SpringBootTest
public class ParticipantInfoServiceTest {
    @Autowired
    ParticipantInfoService participantInfoService;

    Long quizId = 1L;
    int capacity = 100;

    @AfterEach
    void clear() {
        participantInfoService.deleteAll();
    }

    @Test
    void saveFcfsTest() throws InterruptedException {
        int threadCnt = 100;

        CountDownLatch countDownLatch;
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            countDownLatch = new CountDownLatch(threadCnt);

            IntStream.range(0, threadCnt).forEach(e -> executor.execute(() -> {
                try {
                    participantInfoService.saveFcfs(quizId, (long) (e + 1), capacity);
                } finally {
                    countDownLatch.countDown();
                }
            }));
            countDownLatch.await();
        }


        int participantCnt = participantInfoService.countParticipantInfoCntByQuizId(quizId);
        List<ParticipantInfo> participantInfoList = participantInfoService.findParticipantInfoByQuizId(quizId);
        for (ParticipantInfo participantInfo : participantInfoList) {
            log.info("_id : {}",participantInfo.getId());
            log.info("userId : {}", participantInfo.getUserId());
        }

        log.info("participantCnt = {}",participantCnt);
        assertThat(participantCnt)
                .isEqualTo(100);

    }

}
