package com.quiz.api.participant_info;

import com.quiz.domain.participantsinfo.dto.ParticipantsRankResponseDto;
import com.quiz.domain.participantsinfo.service.ParticipantInfoFacade;
import com.quiz.domain.participantsinfo.service.ParticipantInfoQueueService;
import com.quiz.domain.response.dto.ResponsesRequestsDto;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.service.UsersService;
import com.quiz.dto.ResponseDto;
import com.quiz.global.security.userdetails.UserAccount;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/participant_info")
@RestController
public class ParticipantInfoController {
    private final ParticipantInfoFacade participantInfoFacade;
    private final ParticipantInfoQueueService participantInfoQueueService;
    private final UsersService usersService;

    AtomicLong atomicLong = new AtomicLong(1);


    @PostMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> saveParticipant(@PathVariable("quiz-id") Long quizId,
                                                          @AuthenticationPrincipal UserAccount user) {
        // 대기열 로직 추가
        // queue 에 인간 추가
        Users users = usersService.findByEmail(user.getUsername());


        Long rank = participantInfoQueueService.addQueue(quizId, atomicLong.incrementAndGet());
        return ResponseEntity.ok(ResponseDto.success(rank));
    }

    @PatchMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> updateParticipant(@PathVariable("quiz-id") Long quizId,
                                                            @RequestBody ResponsesRequestsDto request,
                                                            @AuthenticationPrincipal UserAccount user) {
        Users users = usersService.findByEmail(user.getUsername());

        participantInfoFacade.updateParticipantAndSaveResponse(quizId, atomicLong.decrementAndGet(), request.getResponses());

        return ResponseEntity.ok(ResponseDto.success());
    }

    @GetMapping("/{quiz-id}")
    public ResponseEntity<ResponseDto<?>> showRanks(@PathVariable("quiz-id") Long quizId,
                                                    @AuthenticationPrincipal UserAccount user) {
        Users users = usersService.findByEmail(user.getUsername());
        participantInfoFacade.checkUserParticipatedOrOwner(quizId, users.getId());

        List<ParticipantsRankResponseDto> response = participantInfoFacade.showRanks(quizId);

        return ResponseEntity.ok(ResponseDto.success(response));
    }
}
