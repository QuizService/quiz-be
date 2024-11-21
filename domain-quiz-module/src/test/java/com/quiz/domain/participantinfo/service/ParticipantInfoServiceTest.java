package com.quiz.domain.participantinfo.service;

import com.quiz.TestConfiguration;
import com.quiz.domain.participantsinfo.service.ParticipantInfoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

// Integration Test
@Testcontainers
@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
public class ParticipantInfoServiceTest {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017);
    static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);
    static MySQLContainer mysqlContainer = new MySQLContainer(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("quiz")
            .withUsername("user")
            .withPassword("password");

    static {
        mongoDBContainer.start();
        redisContainer.start();
        mysqlContainer.start();
    }

    @Autowired
    ParticipantInfoService participantInfoService;
    Long quizId = 1L;
    int capacity = 90;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getReplicaSetUrl("quiz"));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.username", () -> "admin");
        registry.add("spring.data.mongodb.password", () -> "password");
        registry.add("spring.data.mongodb.database", () -> "quiz");

        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);

        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
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
        assertThat(participantCnt)
                .isEqualTo(90);
        assertThat(cnt.get())
                .isEqualTo(10);

    }
}
