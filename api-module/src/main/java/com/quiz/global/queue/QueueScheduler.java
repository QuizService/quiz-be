package com.quiz.global.queue;

import com.quiz.domain.participantsinfo.dto.ParticipantQueueDto;
import com.quiz.domain.participantsinfo.repository.redis.ParticipantInfoQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
//@Transactional
@Component
@RequiredArgsConstructor
public class QueueScheduler {
    private final ParticipantInfoQueueRepository participantInfoQueueRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Scheduled(fixedDelay = 2000)
    public void queue() {
        List<ParticipantQueueDto> participantQueueDtoSet = participantInfoQueueRepository.getUserFromQueue();

        participantInfoQueueRepository.delete();

        for (ParticipantQueueDto participantQueueDto : participantQueueDtoSet) {
            /*
            * user info와 quizInfo를 통해 rank를 수집한다
            *
            * redis에서 선착순이 다 되었는지 확인한다.
            *
            * 이후 정보를 event Publisher로 전달한다.(만약 인원이 다 안 찼으면 순서를 보내고 아니면 안되었다는 메시지를 전송)
            * */
            Long quizId = participantQueueDto.quizId();
            Long userId = participantQueueDto.userId();

            Long rank = participantInfoQueueRepository.getRank(quizId, userId);

            // 아직 자리 다 안찬 경우
            if (participantInfoQueueRepository.getParticipantNumber(quizId) > 0) {
                eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, true));
            } else {
                eventPublisher.publishEvent(new ParticipantQueueInfoDto(quizId, userId, rank, false));
            }

        }
    }
}
