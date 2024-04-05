package com.quiz.domain.users.service;

import com.quiz.domain.users.dto.UserNameDto;
import com.quiz.domain.users.entity.Users;
import com.quiz.domain.users.repository.UsersRepository;
import com.quiz.domain.users.dto.UsersRequestDto;
import com.quiz.global.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.quiz.global.exception.user.code.UserErrorCode.USER_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(value = "mysqlTx")
@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public void save(UsersRequestDto request) {
        Users users = Users.builder()
                .email(request.getEmail())
                .name(request.getName())
                .provider(request.getProvider())
                .build();
        usersRepository.save(users);
    }

    @Transactional(value = "mysqlTx", propagation = Propagation.REQUIRES_NEW)
    public List<UserNameDto> findUsernameDtosByIds(List<Long> userIds) {
        return usersRepository.findUsernameByIds(userIds);
    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }


}
