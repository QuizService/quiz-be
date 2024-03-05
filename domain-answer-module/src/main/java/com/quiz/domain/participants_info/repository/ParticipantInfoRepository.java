package com.quiz.domain.participants_info.repository;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantInfoRepository extends JpaRepository<ParticipantInfo, Long> {

    @Query("SELECT COUNT(p.id) FROM ParticipantInfo p WHERE p.quizId = :quizId")
    Integer countParticipantsByQuizId(@Param("quizId") Long quizId);
}
