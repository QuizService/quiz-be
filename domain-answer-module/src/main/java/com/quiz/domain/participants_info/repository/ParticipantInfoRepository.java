package com.quiz.domain.participants_info.repository;

import com.quiz.domain.participants_info.entity.ParticipantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantInfoRepository extends JpaRepository<ParticipantInfo, Long> {
}
