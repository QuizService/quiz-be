package com.quiz.domain.participantinfo.service;

import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import com.quiz.domain.participantsinfo.service.ParticipantInfoQueueService;
import com.quiz.domain.participantsinfo.service.ParticipantInfoService;
import com.quiz.global.exception.participantinfo.ParticipantInfoException;
import com.quiz.global.lock.DistributedLock;
import com.quiz.global.testContainer.TestContainerConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.quiz.global.exception.participantinfo.code.ParticipantInfoErrorCode.FIRST_COME_FIRST_SERVED_END;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@ExtendWith(TestContainerConfig.class)
@TestConfiguration
@SpringBootTest
public class ParticipantInfoServiceTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017);

    static {
        mongoDBContainer.start();
    }
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");
    }
    @Autowired
    ParticipantInfoService participantInfoService;

    @Autowired
    ParticipantInfoQueueService participantInfoQueueService;

    Long quizId = 1L;
    int capacity = 90;

    @BeforeAll
    static void start() {
        log.info("start test");
    }

    @BeforeEach
    void setUp() {
        participantInfoQueueService.createQuizQueue(1L, capacity);
    }

    @AfterEach
    void clear() {
        participantInfoService.deleteAll();
    }

    @Test
    void saveFcfsTest() throws InterruptedException {
        int threadCnt = 100;
        AtomicInteger cnt = new AtomicInteger();
        CountDownLatch countDownLatch;
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            countDownLatch = new CountDownLatch(threadCnt);

            IntStream.range(0, threadCnt).forEach(e -> executor.execute(() -> {
                try {
                    participantInfoService.saveFcfsTest(quizId, (long) (e + 1), capacity);
                } catch (Exception ex) {
                    cnt.getAndIncrement();
                } finally {
                    countDownLatch.countDown();
                }
            }));
            countDownLatch.await();
        }

        int participantCnt = participantInfoService.countParticipantInfoByQuizId(quizId);
        List<ParticipantInfo> participantInfoList = participantInfoService.findParticipantInfoByQuizId(quizId);
        for (ParticipantInfo participantInfo : participantInfoList) {
            log.info("_id : {}", participantInfo.getId());
            log.info("userId : {}", participantInfo.getUserId());
        }

        log.info("participantCnt = {}", participantCnt);
        assertThat(participantCnt)
                .isEqualTo(90);
        assertThat(cnt.get())
                .isEqualTo(10);

    }

}
