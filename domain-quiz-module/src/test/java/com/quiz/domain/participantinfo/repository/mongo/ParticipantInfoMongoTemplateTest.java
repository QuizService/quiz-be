package com.quiz.domain.participantinfo.repository.mongo;

import com.quiz.TestConfiguration;
import com.quiz.domain.participantsinfo.entity.ParticipantInfo;
import com.quiz.domain.participantsinfo.repository.mongo.ParticipantInfoMongoTemplate;
import com.quiz.domain.participantsinfo.repository.mongo.ParticipantInfoRepository;
import com.quiz.domain.questions.entity.Questions;
import com.quiz.domain.questions.repository.mongo.QuestionsRepository;
import com.quiz.domain.quiz.entity.Quiz;
import com.quiz.domain.quiz.repository.mongo.QuizRepository;
import com.quiz.global.exception.participantinfo.ParticipantInfoException;
import com.quiz.global.exception.participantinfo.code.ParticipantInfoErrorCode;
import com.quiz.global.mock.TestEntities;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import static com.quiz.global.exception.participantinfo.code.ParticipantInfoErrorCode.PARTICIPANT_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@Testcontainers
@DataMongoTest
public class ParticipantInfoMongoTemplateTest {
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

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
        registry.add("spring.data.mongodb.authentication-database", () -> "admin");
    }

    private ParticipantInfoMongoTemplate participantInfoMongoTemplate;
    @Autowired
    private ParticipantInfoRepository participantInfoRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        participantInfoMongoTemplate = new ParticipantInfoMongoTemplate(mongoTemplate);
        Quiz quiz = quizRepository.save(TestEntities.getQuiz());
        questionsRepository.save(TestEntities.getQuestions(quiz.getIdx()));
    }

    @AfterEach
    void clear() {
        questionsRepository.deleteAll();
        quizRepository.deleteAll();
        participantInfoRepository.deleteAll();
    }

    @Test
    void countParticipantsByQuizIdAndSubmitResponsesTest() {
        List<ParticipantInfo> participantInfos = TestEntities.getParticipantInfos();
        participantInfos = participantInfoRepository.saveAll(participantInfos);
        int cnt = participantInfoMongoTemplate.countParticipantsByQuizIdAndSubmitResponses(1L);

        Assertions.assertThat(cnt)
                .isEqualTo(participantInfos.size());

    }

    @Test
    void findByQuizIdAndUserId() {
        save();
        Long userId = 5L;
        int expectedTotalScore = 9;
        int expectedNumber = 5;
        ParticipantInfo participantInfo = ParticipantInfo.testBuilder()
                .quizId(1L)
                .userId(userId)
                .totalScore(expectedTotalScore)
                .number(expectedNumber)
                .submitResponses(true)
                .testBuild();
        participantInfoRepository.save(participantInfo);

        ParticipantInfo findParticipant = participantInfoMongoTemplate.findByQuizIdAndUserId(1L, userId)
                .orElseThrow(() -> new ParticipantInfoException(PARTICIPANT_NOT_FOUND));

        assertThat(findParticipant.getNumber())
                .isEqualTo(expectedNumber);
        assertThat(findParticipant.getTotalScore())
                .isEqualTo(expectedTotalScore);
    }

    @Test
    void updateTest() {
        Long quizId = 1L;
        Long userId = 1L;
        int expectedNumber = 5;
        ParticipantInfo participantInfo = ParticipantInfo.testBuilder()
                .quizId(quizId)
                .userId(userId)
                .testBuild();
        participantInfoRepository.save(participantInfo);

        participantInfoMongoTemplate.update(quizId, userId, expectedNumber);
        ParticipantInfo findParticipant = participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId)
                .orElseThrow(() -> new ParticipantInfoException(PARTICIPANT_NOT_FOUND));

        assertThat(findParticipant.getNumber())
                .isEqualTo(expectedNumber);
        assertThat(findParticipant.isSubmitResponses())
                .isTrue();
    }

    @Test
    void updateScoreTest() {
        Long quizId = 1L;
        Long userId = 1L;
        int expectedNumber = 5;
        int expectedScore = 5;
        ParticipantInfo participantInfo = ParticipantInfo.testBuilder()
                .quizId(quizId)
                .userId(userId)
                .testBuild();
        participantInfoRepository.save(participantInfo);
        ParticipantInfo findParticipant = participantInfoMongoTemplate.findByQuizIdAndUserId(quizId, userId)
                .orElseThrow(() -> new ParticipantInfoException(PARTICIPANT_NOT_FOUND));
        participantInfoMongoTemplate.update(quizId, userId, expectedNumber);
        participantInfoMongoTemplate.updateScore(findParticipant.getId(), expectedScore);

        ParticipantInfo result = participantInfoRepository.findById(findParticipant.getId())
                .orElseThrow(() -> new ParticipantInfoException(PARTICIPANT_NOT_FOUND));
        assertThat(result.getTotalScore())
                .isEqualTo(expectedScore);
    }

    @Test
    void findAllByQuizIdOrderByNumberTest() {
        List<ParticipantInfo> participantInfos = new ArrayList<>();
        Long quizId = 1L;
        for(int i = 0; i< 5; i++) {
            ParticipantInfo participantInfo = ParticipantInfo.testBuilder()
                    .quizId(quizId)
                    .userId((long)i)
                    .totalScore(i)
                    .number(5-i)
                    .submitResponses(true)
                    .testBuild();
            participantInfos.add(participantInfo);
        }
        //순서 역순으로 오도록 저장
        participantInfoRepository.saveAll(participantInfos);

        // 순서 순으로(asc) 정렬된 참여자 리스트 조회
        List<ParticipantInfo> results = participantInfoMongoTemplate.findAllByQuizIdOrderByNumber(quizId);
        List<Integer> numberList = results.stream()
                .map(ParticipantInfo::getNumber)
                .toList();

        // 순서 순으로(asc) 정렬되었는지 확인
        assertThat(results.size())
                .isEqualTo(5);
        for(int i = 0; i<numberList.size(); i++) {
            assertThat(numberList.get(i))
                    .isEqualTo(i+1);
        }

    }

    List<ParticipantInfo> save() {
        return participantInfoRepository.saveAll(TestEntities.getParticipantInfos());
    }
}
